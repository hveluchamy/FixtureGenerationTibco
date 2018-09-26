package Entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyDetailsSingleton {
    private static PropertyDetailsSingleton solePropertyDetails;
    private static String FILENAME = "application.properties";

    public static Properties getApplicationProperty() {
        return applicationProperty;
    }

    private static Properties applicationProperty;
    private PropertyDetailsSingleton(){
        if(solePropertyDetails != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static Properties getPropertyInstance(){
        if(solePropertyDetails ==null){
            solePropertyDetails = new PropertyDetailsSingleton();
            applicationProperty = new Properties();
            InputStream input = null;
            try{
                input = PropertyDetailsSingleton.class.getClassLoader().getResourceAsStream(FILENAME);
                if(input==null){
                    return null;
                }
                applicationProperty.load(input);
            } catch (IOException ex){
                ex.printStackTrace();
            }



        }
        return applicationProperty;
    }
}

//private static Properties properties = PropertyDetailsSingleton.getPropertyInstance();