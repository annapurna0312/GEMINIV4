package com.example.geminiv4.devicedata;


import com.example.geminiv4.utils.GPSDate;

public class Frame0 {

	
	public Frame0(String framedata, int msgid) {
		super();
    	try{

    	    this.mid = msgid;

            int index = 0;
            //framecount
            int framecount = binaryToUnsignedInteger(framedata.substring(index, index + 10));
            index += 10;
            setFramescount(framecount);

            //alert
            int alert = Integer.parseInt(framedata.substring(index, index + 1));
            index += 1;
            if(alert == 1){
                setIsalert(true);
            }else{
                setIsalert(false);
            }


            //priority
            int prioritybit = binaryToUnsignedInteger(framedata.substring(index, index + 3));
            index += 3;
            setPriority(prioritybit);


            //test
            int testbit = binaryToUnsignedInteger(framedata.substring(index, index + 1));
            index += 1;
            if(testbit==1){
                setTest(true);
            }else{
                setTest(false);
            }

            //expiration time
			GPSDate gpsDate = new GPSDate();
            String expirationweek = framedata.substring(index, index + 10);
            index += 10;
            String expsecondofweek = framedata.substring(index, index + 20);
            index += 20;
            String expdatetime = gpsDate.gps2greg(binaryToUnsignedInteger(expirationweek),binaryToUnsignedInteger(expsecondofweek));
            setExpiration(expdatetime);


            //country
            String countrycode = getCountryCodeForBinaryPattern(framedata.substring(index, index + 5));
            index += 5;
            setCountry(countrycode);

            //msg format
            String messfromind = getMessageFormatIndicatorForBinaryPattern(framedata.substring(index, index + 3));
            index += 3;
            setMessageformat(messfromind);

            //sw version
            int swversionindicator = binaryToUnsignedInteger(framedata.substring(index, index + 4));
            index += 4;
            setVersion(swversionindicator);


            //image/text // 0(Text) 1(Image)
            int image_or_text = Integer.parseInt(framedata.substring(index, index + 1));
            index += 1;
            if(image_or_text == 0){
                setText(true);
            }else{
                setText(false);
            }

            //template
            int template = Integer.parseInt(framedata.substring(index, index + 1));
            index += 1;
            if(template == 1){
                setTemplate(true);
            }else{
                setTemplate(false);
            }


            //compression
            int compression = Integer.parseInt(framedata.substring(index, index + 1));
            index += 1;
            if(compression == 1){
                setCompressed(true);
            }else{
                setCompressed(false);
            }


            //originator
            String message_origin_indicator = getMessageOriginatorIndicatorForBinaryPattern(framedata.substring(index, index + 4));
            index += 4;
            setOriginator(message_origin_indicator);


            //typeofservice
            this.typeofservice = getTypeofServiceForBinaryPattern(framedata.substring(index, index + 5));
            index += 5;
            setTypeofservice(typeofservice);

            //zone
			setZone(getZoneIndicatorForBinaryPattern(framedata.substring(index, index + 7)));
            index += 7;


            //spare bits
			setSparebits(framedata.substring(index, index + 116));



        }catch (Exception e){
            e.printStackTrace();
        }	
	}
	
	
	private int mid;
    private int framescount;
    private boolean isalert;
    private int priority;
    private boolean isTest;
    private String expiration;
    private String country;
    private String messageformat;
    private int version;
    private boolean isText; // 0(Text) 1(Image)
    private boolean isTemplate;
    private boolean isCompressed;
    private String originator;
    private String typeofservice;
    private String zone;
    private String sparebits;





    
    
    
    
    
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public int getFramescount() {
		return framescount;
	}
	public void setFramescount(int framescount) {
		this.framescount = framescount;
	}
	public boolean isIsalert() {
		return isalert;
	}
	public void setIsalert(boolean isalert) {
		this.isalert = isalert;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public boolean isTest() {
		return isTest;
	}
	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}
	public String getExpiration() {return expiration;}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMessageformat() {
		return messageformat;
	}
	public void setMessageformat(String messageformat) {
		this.messageformat = messageformat;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public boolean isText() {
		return isText;
	}
	public void setText(boolean isText) {
		this.isText = isText;
	}
	public boolean isTemplate() {
		return isTemplate;
	}
	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	public boolean isCompressed() {
		return isCompressed;
	}
	public void setCompressed(boolean isCompressed) {
		this.isCompressed = isCompressed;
	}
	public String getOriginator() {return originator;}
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	public String getTypeofservice() {
		return typeofservice;
	}
	public void setTypeofservice(String typeofservice) {
		this.typeofservice = typeofservice;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getSparebits() {return sparebits;}
	public void setSparebits(String sparebits) {
		this.sparebits = sparebits;
	}


	public String getNavicAdvice(String code){
		String advice = null;
		if(code.equalsIgnoreCase("00")){
			advice  = "Fishermen not to venture into open seas";
		}
		if(code.equalsIgnoreCase("01")){
			advice  = "Fishermen at Sea are requested to come to nearest ports";
		}
		if(code.equalsIgnoreCase("10")){
			advice  = "Fishermen to be cautious while going out in the sea";
		}
		if(code.equalsIgnoreCase("11")){
			advice  = "Fishermen are advised to return to coast";
		}
		return advice;
	}


	public int binaryToUnsignedInteger(String binary) {
		char[] numbers = binary.toCharArray();
		int result = 0;
		for(int i = numbers.length - 1; i >= 0; --i) {
			if (numbers[i] == '1') {
				result = (int)((double)result + Math.pow(2.0D, (double)(numbers.length - i - 1)));
			}
		}
		return result;
	}



	public String getNavicStateBinaryPattern(String state){
		String binarypattern = "";
		if(state.equalsIgnoreCase("0001")){binarypattern = "Gujarat";}else
		if(state.equalsIgnoreCase("0010")){binarypattern = "Maharashtra";}else
		if(state.equalsIgnoreCase("0011")){binarypattern = "Goa";}else
		if(state.equalsIgnoreCase("0100")){binarypattern = "Karnataka";}else
		if(state.equalsIgnoreCase("0101")){binarypattern = "Kerala";}else
		if(state.equalsIgnoreCase("0110")){binarypattern = "SouthTamilNadu";}else
		if(state.equalsIgnoreCase("0111")){binarypattern = "NorthTamilNadu";}else
		if(state.equalsIgnoreCase("1000")){binarypattern = "SouthAndhraPradesh";}else
		if(state.equalsIgnoreCase("1000")){binarypattern = "NorthAndhraPradesh";}else
		if(state.equalsIgnoreCase("1001")){binarypattern = "Odisha";}else
		if(state.equalsIgnoreCase("1010")){binarypattern = "WestBengal";}else
		if(state.equalsIgnoreCase("1011")){binarypattern = "Andaman";}else
		if(state.equalsIgnoreCase("1100")){binarypattern = "Nicobar";}else
		if(state.equalsIgnoreCase("1101")){binarypattern = "Lakshadweep";}
		return binarypattern;
	}











	public static String getCountryCodeForBinaryPattern(String binary){
		String country = "";
		if(binary.equalsIgnoreCase("00000")){
			country = "india";
		}else if(binary.equalsIgnoreCase("00001")){
			country = "nepal";
		}else if(binary.equalsIgnoreCase("00010")){
			country = "bangladesh";
		}else if(binary.equalsIgnoreCase("00011")){
			country = "bhutan";
		}else{
			country = "srilanka";
		}
		return country;
	}

	public static String getMessageFormatIndicatorForBinaryPattern(String binary){
		String ret = "";
		if(binary.equalsIgnoreCase("000")){
			ret = "india";
		}else{
			ret = "egnos";
		}
		return ret;
	}

	public static String getMessageOriginatorIndicatorForBinaryPattern(String binary){
		String ret = "";
		if(binary.equalsIgnoreCase("0000")){
			ret = "incois";
		}else{
			ret = "";
		}
		return ret;
	}

	public static String getTypeofServiceForBinaryPattern(String binary){
		String ret = "";
		if(binary.equalsIgnoreCase("00000")){ret = "TSUNAMI";}
		if(binary.equalsIgnoreCase("00001")){ret = "PFZ";}
		if(binary.equalsIgnoreCase("00010")){ret = "OSF";}
		if(binary.equalsIgnoreCase("00011")){ret = "CYCLONE";}
		if(binary.equalsIgnoreCase("00100")){ret = "FishMarketData";}
		if(binary.equalsIgnoreCase("00101")){ret = "STRONGWINDALERT";}
		if(binary.equalsIgnoreCase("11111")){ret = "ERASER";}
		return ret;
	}

	public static String getZoneIndicatorForBinaryPattern(String binary){
		String ret = "";
		if(binary.equalsIgnoreCase("0000001")){ret =  "Gujarat";}else
		if(binary.equalsIgnoreCase("0000010")){ret =  "Maharashtra";}else
		if(binary.equalsIgnoreCase("0000011")){ret =  "Goa";}else
		if(binary.equalsIgnoreCase("0000100")){ret =  "Karnataka";}else
		if(binary.equalsIgnoreCase("0000101")){ret =  "Kerala";}else
		if(binary.equalsIgnoreCase("0000110")){ret =  "SouthTamilNadu";}else
		if(binary.equalsIgnoreCase("0000111")){ret =  "NorthTamilNadu";}else
		if(binary.equalsIgnoreCase("0001000")){ret =  "SouthAndhraPradesh";}else
		if(binary.equalsIgnoreCase("0001001")){ret =  "NorthAndhraPradesh";}else
		if(binary.equalsIgnoreCase("0001010")){ret =  "Odisha";}else
		if(binary.equalsIgnoreCase("0001011")){ret =  "WestBengal";}else
		if(binary.equalsIgnoreCase("0001100")){ret =  "Andaman";}else
		if(binary.equalsIgnoreCase("0001101")){ret =  "Nicobar";}else
		if(binary.equalsIgnoreCase("0001110")){ret =  "Lakshadweep";}else
		if(binary.equalsIgnoreCase("0001111")){ret =  "EastCoast";}else
		if(binary.equalsIgnoreCase("0010000")){ret =  "WestCoast";}else
		if(binary.equalsIgnoreCase("0010001")){ret =  "ALLINDIA";}
		return ret;
	}

	@Override
	public String toString() {
		return "Frame0{" +
				"mid=" + mid +
				", framescount=" + framescount +
				", isalert=" + isalert +
				", priority=" + priority +
				", isTest=" + isTest +
				", expiration='" + expiration + '\'' +
				", country='" + country + '\'' +
				", messageformat='" + messageformat + '\'' +
				", version=" + version +
				", isText=" + isText +
				", isTemplate=" + isTemplate +
				", isCompressed=" + isCompressed +
				", originator='" + originator + '\'' +
				", typeofservice='" + typeofservice + '\'' +
				", sparebits='" + sparebits + '\'' +
				", zone='" + zone + '\'' +
				'}';
	}

}
