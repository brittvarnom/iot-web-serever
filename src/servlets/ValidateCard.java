package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// A very basic Sensor Server.
// Simply stores the last sensor name and value in memory.
//No data is permanently stored.

@WebServlet("/ValidateCard")

public class ValidateCard extends HttpServlet {
	
    // Collects or returns data for sensorname, sensorvalue parameters
	private static final long serialVersionUID = 1L;
	
	// Local variables holding last values stored for each parameter
	private String lastValidSensorNameStr  = "no sensor";
    private String lastValidSensorValueStr = "invalid";

    public ValidateCard() {
        super();
    }
	  public void init(ServletConfig config) throws ServletException {
		  System.out.println("Sensor server is up and running\n");	
		  System.out.println("Upload sensor data with http://localhost:8080/PhidgetServer2019/SensorServer?sensorname=xxx&sensorvalue=nnn");
		  System.out.println("View last sensor reading at  http://localhost:8080/PhidgetServer2019/SensorServer?getdata=true \n\n");		  
	  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setStatus(HttpServletResponse.SC_OK);

	    // The client will either send some new sensor data to store, or request existing sensor data
	    // Parameter "getdata" is checked. If it exists with any value, then the most recent
	    // sensor data is returned. Else, the parameters sensorname and sensorvalue are examined for new data
	    
	    // Check to see whether the client is requesting data or sending it
	    String getdata = request.getParameter("getdata");

	    // if no getdata parameter, client is sending data, so get sensorname and sensorvalue
	    if (getdata == null){
	    		// getdata is null, therefore it is receiving data
			String sensorNameStr = request.getParameter("sensorname");
			String sensorValueStr = request.getParameter("sensorvalue");
			if (!(sensorNameStr==null) && !(sensorValueStr==null)) {
				// update local variables and send confirmation back to user
			      PrintWriter out = response.getWriter();
			      out.println(updateSensorValues(sensorNameStr, sensorValueStr));
			      out.close();

			}
		} // end if getdata is null
	    else {  // Display current data (JSON format)
	    	   sendJSONString(response);
	    }

	}

	private String updateSensorValues(String sensorNameStr, String sensorValueStr){
		// all ok, update last known values and return
		lastValidSensorNameStr = sensorNameStr;
		lastValidSensorValueStr = sensorValueStr;
		System.out.println("DEBUG : Updated last sensor to: " + sensorNameStr + ", with value "+sensorValueStr +"yo");
		return "Sensor value updated.";
	}	
	
	
	private void sendJSONString(HttpServletResponse response) throws IOException{
	      response.setContentType("text/plain");
	      
	      // Manually code return json string. NOTE: Better to used Gson library
	      String json = "{\"sensor\": {\"" + lastValidSensorNameStr + 
    		      "\": \"" + lastValidSensorValueStr + "\"}}";
	      String returnTextMessage = "Latest values - Sensor: "+lastValidSensorNameStr +
	    		  						 ", Value: "+lastValidSensorValueStr;
	      
	      PrintWriter out = response.getWriter();
	      System.out.println("DEBUG: sensorServer JSON: "+json);
	      System.out.println("DEBUG: sensorServer TEXT: "+returnTextMessage);
	      
	      // Change below to return json or text to the browser
	      //out.println(json);
	      out.println(returnTextMessage);
	      out.close();
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	}

}

