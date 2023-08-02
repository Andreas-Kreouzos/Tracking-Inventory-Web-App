package gr.iag.dgtl.inventory

class PropertyReader {

    private static Properties props = new Properties()

    static def loadProperties(){
        InputStream is = PropertyReader.class.getResourceAsStream('/test.properties')
        props.load(is)
    }

    static String getProperty(String key){
        if(props.isEmpty()) loadProperties()
        props.getProperty(key)
    }
}
