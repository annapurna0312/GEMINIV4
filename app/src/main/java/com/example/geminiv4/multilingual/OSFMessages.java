package com.example.geminiv4.multilingual;

import android.content.Context;

import com.example.geminiv4.utils.LatLonUtils;

public class OSFMessages {


    public String getHighWindAlert(int selectedlanguage, String message, Context context){
        String[] data = message.split("\\,");
        String msg = "";
        String state = data[0];
        String speed = data[1];
        String direc = data[2];
        String hours = data[3];

        MultiLingualUtils multiLingualUtils = new MultiLingualUtils();
        LatLonUtils latLonUtils = new LatLonUtils();

        switch (selectedlanguage) {
            case 0:
                //en
                msg =   "Strong winds from "+latLonUtils.parseCardinalDirection(direc,context)+" direction, " +
                        "speed reaching "+speed+" kmph likely along and off "+multiLingualUtils.getZoneFromStringsFile(state,context)+" coasts.\n" +
                        "Fishermen are advised not to venture into the sea for next "+hours+" hours.";
                break;
            case 1:
                //hindi
                msg = multiLingualUtils.getZoneFromStringsFile(state,context)+" तट पर तेज़ हवाएं "+latLonUtils.parseCardinalDirection(direc,context)+" दिशा से "+
                     speed+" किलोमीटर प्रति घंटा से पहुँच सकती है \n" +"मछुआरों को अगले "+hours+" घंटों के लिए समुद्र में न जाने की सलाह दी जाती है।";
                break;
            case 2:
                //gu
                msg =    multiLingualUtils.getZoneFromStringsFile(state,context)+" કિનારે "+latLonUtils.parseCardinalDirection(direc,context)+" દિશાથી "+speed+
                                " કિલોમીટર પ્રતિ કલાકની ઝ્ડપે ભારે પવનો ફુંકાવાની સંભાવના છે.\n "+
                                "માછીમારોને આવતા "+hours+" કલાક સુધી દરિયો ન ખેડવા સુચવવામાં આવે છે.";
                break;
            case 3:
                //mr
                msg =   "अंदाज आहे "+multiLingualUtils.getZoneFromStringsFile(state,context)+" च्या किनारपट्टीने जास्त वारा राहील ज्याचे वेग "+
                        speed+" कि. मी. प्रति तास व दिशा "+latLonUtils.parseCardinalDirection(direc,context)+" असेल. " +
                        "मासेमाऱ्यांना सल्ला दिला जातो की पुढील "+hours+" तासांकरीता समुद्रात जाऊ नये.";
                break;
            case 4:
                //ka
                msg =   " ಬಲವಾದ ಗಾಳಿ "+direc+" ನಿರ್ದೇಶನ, " +
                        " ವೇಗ ತಲುಪುತ್ತಿದೆ "+speed+" ಗಂಟೆಗೆ ಕಿಲೋಮೀಟರ್ ಸಾಧ್ಯತೆ ಇದೆ "+multiLingualUtils.getZoneFromStringsFile(state,context)+" ಕಡಲತೀರಗಳು.\n" +
                        " ಮೀನುಗಾರರು ಮುಂದಿನದಕ್ಕೆ ಸಮುದ್ರಕ್ಕೆ ಇಳಿಯದಂತೆ ಸೂಚಿಸಲಾಗಿದೆ "+hours+" ಗಂಟೆಗಳು.";
                break;
            case 5:
                //ml
                msg =   "മണിക്കൂറിൽ "+speed+" കി. മീ. വേഗതയുള്ള, "+latLonUtils.parseCardinalDirection(direc,context)+" നിന്നും ഉള്ള, ശക്തമായ കാറ്റ് , " +
                        ""+multiLingualUtils.getZoneFromStringsFile(state,context)+" ൻറെ തീരത്തും കടലിലും വീശാനിടയുണ്ട്.\n" +
                        "മീൻപിടുത്തക്കാർ വരുന്ന "+hours+" മണിക്കൂർ നേരത്തേക്ക് കടലിൽ പോകരുതെന്ന് ഉപദേശിക്കുന്നു. ";
                break;
            case 6:
                //ta
                msg =   " இருந்து பலத்த காற்று "+direc+" திசையில், " +
                        " வேகம் அடையும் "+speed+" ஒரு மணி நேரத்திற்கு ஒரு கிலோமீட்டர் "+multiLingualUtils.getZoneFromStringsFile(state,context)+" கடலோரங்களில்.\n" +
                        " மீனவர்கள் அடுத்ததாக கடலுக்குள் செல்ல வேண்டாம் என்று அறிவுறுத்தப்படுகிறார்கள் "+hours+" மணி.";
                break;
            case 7:
                //te
                msg = multiLingualUtils.getZoneFromStringsFile(state,context)+" తీరానికి గంటకు "+speed+" కిలోమీటర్ల వేగం గల బలమైన గాలులు "+
                        ""+latLonUtils.parseCardinalDirection(direc,context)+" దిశగా వచ్చే సూచన కలదు. కావున" +
                        "మత్స్యకారులు తరువాతి " + hours + " గంటలు సముద్రంలోకి ప్రవేశించకూడదని సూచించడమైనది.";
                break;
            case 8:
                //or
                msg =   "ତୀବ୍ର ପବନ ବହିବ "+latLonUtils.parseCardinalDirection(direc,context)+"  ଦିଗ ରୁ, " +
                        "ବେଗ ଛୁଇଁ ପାରେ "+speed+"  କି.ମି. ପ୍ରତି ଘଣ୍ଟା "+multiLingualUtils.getZoneFromStringsFile(state,context)+" ଉପକୂଳ ଓ ତଟବର୍ତ୍ତୀ ଅଂଚଳ ରେ \n" +
                        "ଆଗାମୀ "+hours+" ଘଣ୍ଟା ପର୍ଯ୍ୟନ୍ତ ସମୁଦ୍ର ଭିତରକୁ ନଯିବା ପାଇଁ ଧୀବର ମାନଙ୍କୁ ପରାମର୍ଶ ଦିଆ ଯାଉଛି ";
                break;
            case 9:
                //bn
                msg =   " থেকে শক্তিশালী বাতাস "+direc+" অভিমুখ, " +
                        " গতি পৌঁছেছেন "+speed+" প্রতি ঘন্টায় এবং বন্ধ প্রতি ঘন্টায় কিলোমিটার "+multiLingualUtils.getZoneFromStringsFile(state,context)+
                        " উপকূল.\n" +
                        " মৎস্যজীবিদের সমুদ্রে যাওয়া থেকে বিরত থাকতে অনুরোধ করা হচ্ছে পরের জন্য "+hours+" ঘন্টার.";
                break;
        }
        return msg;
    }

    public String getHWAMessage(int selectedlanguage, String message, Context context){
        String[] data = message.split(",");
        String msg = "";
        String fromdate = data[0];        String todate = data[1];   String fromhr = data[2];        String tohr = data[3];
        String range1 = data[4];        String range2 = data[5];     String cs1 = data[6];        String cs2 = data[7];
        String coast = data[8];
        String fromloc = data[9].substring(0, 1).toUpperCase() + data[9].substring(1);
        String toloc = data[10].substring(0, 1).toUpperCase() + data[10].substring(1);
        MultiLingualUtils multiLingualUtils = new MultiLingualUtils();
        FishLandingCenter fishLandingCenter = new FishLandingCenter();
        switch (selectedlanguage) {
            case 0:
                //en
                msg = "High waves in the range of "+data[4]+" - "+data[5]+" meters are forecasted during " +
                        data[2]+" hours on "+data[0]+" to "+data[3]+" hours of "+data[1] +
                        " along the coast of " +
                        data[8]+" " +
                        "from " +
                        data[9]+" to "+data[10]+". " +
                        "Current speeds vary between "+data[6]+" - "+data[7]+" cm/sec.";
                break;
            case 1:
                //hindi
                msg = "उच्च लहरों की ऊंचाई  का पूर्वानुमान "+range1+" - "+range2+" मीटर समय "+fromhr+" दिनांक "+fromdate+" से "+tohr+" दिनांक "+todate+" तक " +
                        multiLingualUtils.getZoneFromStringsFile(coast,context)+" के तट "+
                        fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+" से " +
                        fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+" तक किया गया है| प्रवाह वेग "+cs1+" - "+cs2+" सेंटीमीटर/सेकंड के बीच बदलता रहता है|";
                break;
            case 2:
                //gu
                msg = "તારિખ "+fromdate+" ના "+fromhr+" કલાક થી માંડી ને તારિખ "+todate+" "+tohr+" કલાક સુધી, જખૌ થી દિવ વચ્ચેના કાંઠા-વિસ્તાર માં "+range1+" - "+range2+" ફ" +
                        "ીટ સુધી ઉંચાઇ ના મોજા ઉછળવાની ભારે સંભાવના રહેલી છે.";
                break;
            case 3:
                //mr
                msg =   "अंदाज आहे की, दिनांक "+todate+" ला "+fromhr+" पासून "+tohr+" पर्यंत "+multiLingualUtils.getZoneFromStringsFile(coast,context)+"च्या  " +
                        "किनारपट्टीने  "+fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+" ते "+
                        fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+" पर्यंत, लाटांची उंची "+range1+" पासून "+range2+" पर्यंत राहील.";
                break;
            case 4:
                //ka
                msg = " ವ್ಯಾಪ್ತಿಯಲ್ಲಿ ಹೆಚ್ಚಿನ ಅಲೆಗಳು "+range1+" - "+range2+" ಸಮಯದಲ್ಲಿ ಮೀಟರ್ ಮುನ್ಸೂಚನೆ ನೀಡಲಾಗುತ್ತದೆ "+fromhr+" ಗಂಟೆಗಳ ಮೇಲೆ "+fromdate+" ಗೆ " +
                        tohr+" ಗಂಟೆಗಳ "+todate+" ಕರಾವಳಿಯುದ್ದಕ್ಕೂ "+
                        multiLingualUtils.getZoneFromStringsFile(coast,context)+" ನಿಂದ "+fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+" ಗೆ "+
                        fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+".";
                break;
            case 5:
                //ml
                msg = multiLingualUtils.getZoneFromStringsFile(coast,context)+", "+fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+
                        " മുതൽ "+fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+" വരെ  തീരദേശത്ത് "+
                        fromdate+", "+fromhr+" നും "+todate+", "+tohr+" നും ഇടയിൽ, കാറ്റിന്റെ ഫലമായി "+
                        range1+" മുതൽ "+range2+" മീറ്റർ വരെ ഉയരമുള്ള തിരമാലകൾ  അടിക്കുമെന്ന്  പ്രവചിക്കപ്പെടുന്നു. \n" +
                        "നീരൊഴുക്ക്\u200C "+cs1+" മുതൽ "+cs2+" സെ. മീ. / സെക്കൻഡ് വരെ.";
                break;
            case 6:
                //ta
                msg = multiLingualUtils.getZoneFromStringsFile(coast,context)+"  கீழ்க்கண்ட  பகுதிகளில்  "
                        +fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+" - "+fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+"\n" +
                        fromdate+" "+fromhr+" மணி முதல் "+todate+" "+tohr+" மணி வரை பேரலைகள் "+
                        range1+" - "+range2+" அடி உயரத்திற்கு இருக்கும் என முன்னரிவிக்கபடுகிறது.";
                break;
            case 7:
                //te
                msg = multiLingualUtils.getZoneFromStringsFile(coast,context)+" తీరప్రాంత ప్రజలకు ఇన్ కాయిస్ వారు అందిస్తున్న సమాచార సూచనా  "+fromdate+" ("+fromhr+") " +
                        "నుండి "+todate+" ("+tohr+") తేదిలలో "+multiLingualUtils.getZoneFromStringsFile(coast,context)+" తీరానికి "
                        +fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+"  నుంచి "+
                        fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)+" వరకు " +
                        range1+"  -  "+range2+" అడుగుల ఎత్తున అలలు వచ్చే సూచన కలదు.";
                break;
            case 8:
                //or
                msg = multiLingualUtils.getZoneFromStringsFile(coast,context)+" ଉପକୂଳ ଓ ତଟବର୍ତ୍ତୀ ଅଂଚଳ ରେ "+
                        fishLandingCenter.getMultiLingualFLC(fromloc,selectedlanguage)+" ଠାରୁ "+fishLandingCenter.getMultiLingualFLC(toloc,selectedlanguage)
                        +"ପର୍ଯ୍ୟନ୍ତ ଉଚ୍ଚ ତରଙ୍ଗ ର ବ୍ୟବଧାନ "+range1+" - "+range2+" ମିଟର ହେବାର ପୂର୍ବାନୁମାନ କରାଯାଉଛି " +
                        fromdate+" "+fromhr+" ଘଣ୍ଟା ରୁ "+todate+"  "+tohr+" ଘଣ୍ଟା ପର୍ଯ୍ୟନ୍ତ";
                break;
            case 9:
                //bn
                msg = "পশ্চিমবঙ্গের উপকূলবর্তী অঞ্চলে দীঘা থেকে কোনার মধ্যবর্তী অঞ্চলে "+fromdate+" তারিখ "+fromhr+" hrs থেকে আগামি "+todate+" তারিখ "+tohr+" " +
                        "hrs পর্যন্ত সমুদ্রে ঢেউয়ের উচ্চতা থাকবে "+range1+"-"+range2+" feets।";
                break;
        }
        return msg;
    }



    public String getFisherMenAlert(int selectedlanguage){
        String msg = "";
        switch (selectedlanguage) {
            case 0:
                //en
                msg = "Fishermen are advised to be cautious while venturing into sea.";
                break;
            case 1:
                //hindi
                msg = "मछुआरों को सलाह दी जाती है, कि समुंद्र मे जाते वक्त सावधान रहेंl";
                break;
            case 2:
                //gu
                msg = "માછીમારોએ દરિયામાં જતી વખતે સાવધ રહેવું.";
                break;
            case 3:
                //mr
                msg = "मच्छीमारांना सल्ला दिला जातो की समुद्रात जाण्याचे धाडस करू नये.";
                break;
            case 4:
                //ka
                msg = "ಮೀನುಗಾರರಲ್ಲಿ ವಿನಂತಿಸಿ ಕೊಳ್ಳುವುದೇನೆಂದರೆ ಬಹಳ ಎಚ್ಚರಿಕೆ ಇಂದ ವ್ಯವಹರಿಸಬೇಕಾಗಿ ತಮ್ಮೆಲರಲ್ಲೂ ವಿನಂತಿಸಿಕೊಳ್ಳುತಿದ್ದೇವೆ.";
                break;
            case 5:
                //ml
                msg = "മീൻപിടിത്തക്കാർ കടലിൽ പോകരുതെന്ന് ഉപദേശിക്കുന്നു .";
                break;
            case 6:
                //ta
                msg = " மீனவர்கள் கடலில், எச்சரிக்கையுடன்  செயல்பட வேண்டி, கேட்டுக்கொள்ளப்படுகி றாா்கள். ";
                break;
            case 7:
                //te
                msg = "మత్స్యకారులు సముద్రం లోనికి  వెళ్ళునపుడు జాగ్రత్తగా వ్యవహరించవలెనని సూచించటమైనది";
                break;
            case 8:
                //or
                msg = "ସମୁଦ୍ର ଭିତରକୁ ଗଲା ବେଳେ ମାଛଧରାଳି ମାନଂକୁ ସଚେତନ ରହିବାକୁ ପରାମର୍ଶ  ଦିଆଯାଉଛି";
                break;
            case 9:
                //bn
                msg = " মৎস্যজীবিদের সমুদ্রে যাওয়ার সময় সতর্ক থাকার অনুরোধ করা হচ্ছে ";
                break;
        }
        return msg;
    }
}
