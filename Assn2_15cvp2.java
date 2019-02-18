/* 
 * Assignment 2 CMPE 212 Winter 2019
 * Claire Phillips 20010910
 * Febuary 15th 2019
 *  15cvp2
 */


import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Assn2_15cvp2 {
	
	public static int numOfPulses(double[] motorArray, int rows) { //number of pulses from a motor
		int num = 0;
		for (int i = 1; i < (rows-1); i++) {
			if ((motorArray[i] > 1) && (motorArray[i-1] <1)) {
				num++;
			}
		}
		return num;
	}

	public static double[] onOffTimes(double[] motorArray, double[] timeArray, double oldEnd, int rows) { //single on and off time of a pulse
		double[] times = {0 , 0};
		for (int i = (int) Math.round(oldEnd); (i < rows-1) && (times[0] == 0); i++) { // if we havent gotten to the end of the array or start and end havent been updated
			if( motorArray[i] > 1 && motorArray[i -1] < 1 )
				times[0] = timeArray[i]; //element 0 is start of pulse
			
		}
		for (int j = (int) Math.round(times[0]); j< rows-1 && times[1] == 0; j++) { // if we have a start time, finding the end time
			if (motorArray[j] > 1 && motorArray[j+1] < 1 || j == rows-1) //if the next element is 0 this one the end time
				times[1] = timeArray[j]; //element 1 is end of pulse
		 }
		return times;
	}
	
	public static double averageCurrent(double[] motorArray, double onTime, double offTime) { //finding the average current of a pulse
		double sum=0, current;
		for(int i = (int) Math.round(onTime); i <= Math.round(offTime); i++) {
			sum += motorArray[i];
		}
		current = sum*1000/(offTime - onTime + 1.000); //average current is the sum of all currents over the difference between on and off time + 1
		current = (int) Math.round(current);
		current = current/1000;
		return current;
	}
	
	
	public static double[][] currentsTable (double[] motorArray, double[] timeArray, int rows){ //creates table with on time, off time and average current
		double start = 0;
		int pulseNum = numOfPulses(motorArray, rows);
		double[][] table = new double[pulseNum][3]; //make an array with the number of pulses as the number of rows
		for ( int i = 0; i < pulseNum; i++) {
			for (int j=0; j<2; j++) {
				table[i][j] = (int) Math.round(onOffTimes(motorArray, timeArray, start, rows)[j]);
			}
			table[i][2]= averageCurrent(motorArray, table[i][0], table[i][1]); //finding the average current between the on and off time 
			start = table[i][1]; //now the next row will start counting on off values at the last end time
		}
		return table;
	}
	
	public static void writeFile(int motorNumber, double[] motorArray, double[] timeArray, int rows) { //make file
		double[][] currents = currentsTable( motorArray, timeArray, rows);
		Path file = Paths.get("Motor" + motorNumber + ".csv");  //not sure exactly
		try (BufferedWriter writer = Files.newBufferedWriter(file)) {
			if (currents.length == 0) { //if not used 
				writer.write("Not Used");
				return;
			}
			else { //if used
				writer.write("start (sec), finish (sec), current (amps) \r\n"); //make top of table
				for( int i = 0; i < currents.length; i++) { //print each row
					if (currents[i][2] > 8) //if any average current is above 8 
						writer.write(currents[i][0] + "," + currents[i][1] + "," + currents[i][2] + ", ***Current Exceeded*** \r\n");
					else 
						writer.write(currents[i][0] + "," + currents[i][1] + "," + currents[i][2] + "\r\n");
				}
			}
		} catch (IOException err) {
			System.err.println(err.getMessage());
		}
	}
	
	public static double[][] readlog(int rows, int columns) { //imports data from logger file
		double[][] inputArray= new double[rows][columns];
		Path data = Paths.get("Logger.csv");   
		String dataLine;
		try (BufferedReader reader = Files.newBufferedReader(data)) {
			for (int i = 0; i< rows; i++) {
				dataLine = reader.readLine();
				String[] stringArray = dataLine.split(",");
				for ( int j = 0; j < columns; j++) {
					double input=Double.parseDouble(stringArray[j]);
					inputArray[i][j]=input;
				}
			}
		} catch (IOException e) { //error message if exception is caught
			System.err.print(e.getMessage());
		}catch (NumberFormatException e) {
			System.err.print(e.getMessage());
		}
			
		return inputArray;
	}
	
	public static void fillArrays(int rows, int columns) {
		double[] timeArray = new double[rows];
		double[] Motor1 = new double[rows];
		double[] Motor2 = new double[rows];
		double[] Motor3 = new double[rows];
		double[] Motor4 = new double[rows];
		double[] Motor5 = new double[rows];
		double[] Motor6 = new double[rows];
		double[] Motor7 = new double[rows];
		
		for (int i = 0; i< rows; i++) {
			timeArray[i] = readlog( rows, columns)[i][0];
			Motor1[i] = readlog( rows, columns)[i][1];
			Motor2[i] = readlog( rows, columns)[i][2];
			Motor3[i] = readlog( rows, columns)[i][3];
			Motor4[i] = readlog( rows, columns)[i][4];
			Motor5[i] = readlog( rows, columns)[i][5];
			Motor6[i] = readlog( rows, columns)[i][6];
			Motor7[i] = readlog( rows, columns)[i][7];
		}
		writeFile( 1 ,  Motor1, timeArray, rows);
		writeFile( 2 ,  Motor2, timeArray, rows);
		writeFile( 3 ,  Motor3, timeArray, rows);
		writeFile( 4 ,  Motor4, timeArray, rows);
		writeFile( 5 ,  Motor5, timeArray, rows);
		writeFile( 6 ,  Motor6, timeArray, rows);
		writeFile( 7 ,  Motor7, timeArray, rows);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int rows = 1000, columns = 8;
		fillArrays(rows, columns);
		System.out.printf("complete");
	}

	
}
