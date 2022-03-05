package tr.edu.gtu.cse222.project.group8.system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public abstract class FileHelper{
    private FileHelper() {
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Trim the line string according to its data category.
     * @param line in the file
     * @param dataCategory to return (1-2-3-...)
     * @return the requested data 
     */
    public static String trimLine(String line, int dataCategory){
        String[] temp = line.split(";");
        if(dataCategory > temp.length || dataCategory < 0) throw new ArrayIndexOutOfBoundsException();
        return(temp[dataCategory-1]);
    }

    /**
     * Remove from file if the given ID exist.
     * @param file to remove from
     * @param ID of the object
     * @return true if removed, false otherwise
     */
    public static boolean removeByID(File file, String id){
        File newFile = new File(CouponTradingSystem.baseDirectory + "tempFile.txt");
        boolean removed = false;
        int i = 0;

		try {
            Scanner reader = new Scanner(file);
            FileWriter writer = new FileWriter(newFile);
            while(reader.hasNextLine()){
                String data = reader.nextLine();

                if(FileHelper.trimLine(data,1).equals(id)){
                    removed = true;
                }

                else{
                    if(i != 0 ) writer.append("\n");
                    writer.append(data);
                }

                i++;
            }
            
            writer.close();
            reader.close();
            file.delete();
            newFile.renameTo(file);
            return removed;
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return false;
    }

    /**
     * Append line to the given file.
     * @param file to append
     * @param line for append to file.
     * @return true if succesfull, false otherwise.
     */
    public static boolean addLine(File file, String line){
        try {
            Scanner reader = new Scanner(file);
            FileWriter writer = new FileWriter(file, true);
            if(reader.nextLine() != null) writer.append("\n");  //check if it is the first line of the file.
            
            writer.append(line);
            writer.close();
            reader.close();
        } 
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }

        return true;
    }

    /**
     * Checks if given id exist in the given file.
     * @param file to check
     * @param id to check if exist
     * @return true if exist, false otherwise
     */
    public static boolean checkID(File file, String id){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                System.out.println("ZAAAAAAAAAAAAAAAAA  "+ data+ "  AAAAAAAAAAAAA"+id);
                if(FileHelper.trimLine(data,1).equals(id)){
                    reader.close();
                    return true;
                }
            }
            
            reader.close();
            return false;
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return false;
    }

    /**
     * Checks if given data field exist in the given file.
     * @param file to check
     * @param dataField to check if exist
     * @param dataCategory to indicate which field is it.
     * @return true if exist, false otherwise
     */
    public static boolean checkDataField(File file, String dataField, int dataCategory){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                String data = FileHelper.trimLine(line,dataCategory);
                if(data != null && data.equals(dataField)){
                    reader.close();
                    return true;
                }
            }

            reader.close();
            return false;
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return false;
    }

    /**
     * Return the requested data of the given name.
     * @param file to check
     * @param id to check
     * @param dataCategory to indicate which field is it.
     * @return data field if exist.
     */
    public static String returnDataFieldByName(File file, String name, int dataCategory){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                
                if(FileHelper.trimLine(line,2).equals(name)){
                    reader.close();
                    return(FileHelper.trimLine(line, dataCategory));
                }
            }

            reader.close();
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return null;
    }
    /**
     * Return the requested data of the given customer ID.
     * @param file to check
     * @param id to check
     * @param dataCategory to indicate which field is it.
     * @return data field if exist.
     */
    public static String returnDataFieldByCustomerID(File file, String name, int dataCategory){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                
                if(FileHelper.trimLine(line,3).equals(name)){
                    reader.close();
                    return(FileHelper.trimLine(line, dataCategory));
                }
            }

            reader.close();
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return null;
    }
    /**
     * Return the requested data of the given id.
     * @param file to check
     * @param id to check
     * @param dataCategory to indicate which field is it.
     * @return data field if exist.
     */
    public static String returnDataFieldByID(File file, String id, int dataCategory){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                
                if(FileHelper.trimLine(line,1).equals(id)){
                    reader.close();
                    return(FileHelper.trimLine(line, dataCategory));
                }
            }

            reader.close();
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return null;
    }

    /**
     * Return the requested line of the given id.
     * @param file to check
     * @param id to check
     * @return line if exist.
     */
    public static String[] returnLine(File file, String id){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                
                if(FileHelper.trimLine(line,1).equals(id)){
                    reader.close();
                    return(line.split(";"));

                }
            }

            reader.close();
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return null;
    }
    /**
     * Return the requested line of the data.
     * @param file to check
     * @param data to check
     * @return line if exist.
     */
    public static String[] returnLine(File file, String data,int dataCategory){
		try {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine();
                
                if(FileHelper.trimLine(line,dataCategory).equals(data)){
                    reader.close();
                    return(line.split(";"));

                }
            }

            reader.close();
        }
        
        catch (IOException e) {
            System.err.println("Error occured while reading/writing from/to file.\n");
        }
     
        return null;
    }
    
    

}

