package com.souche.ste.util;

import java.util.HashMap;
import java.util.Map;

/**
 * this class defines the domain tail pattern
 * @author wenming.hong
 * @since 2012-7-12
 */
public class DomainMap {
    private Map<String, String> funcDomainMap = new HashMap<String, String>();
    private Map<String, String> countryDomainMap = new HashMap<String, String>();
    private Map<String, Integer> domainTailMap = new HashMap<String, Integer>();
    
    /**
     * constructor
     */
    public DomainMap() {
        initFuncDomainMap();
        initCountryDomainMap();
        initDomainTailMap();
        generateDomainTailMap();
    }

    /**
     * is good domain tail
     * @param domainTail
     * @return true if good, false else
     */
    public boolean isDomainTail(String domainTail) {
        if(domainTail == null || domainTail.length() == 0) {
            return false;
        }
        
        return domainTailMap.containsKey(domainTail);
    }
    
    
    public boolean isCountryDomainTail(String domainTail) {
        if(domainTail == null || domainTail.length() == 0) {
            return false;
        }
        
        return countryDomainMap.containsKey(domainTail);
    }

    /**
     * init country domain map
     */
    private void initCountryDomainMap() {
        countryDomainMap.put("ad", "Andorra");
        countryDomainMap.put("ae", "United Arab Emirates");
        countryDomainMap.put("af", "Afghanistan");
        countryDomainMap.put("ag", "Antigua and Barbuda");
        countryDomainMap.put("ai", "Anguilla");
        countryDomainMap.put("al", "Albania");
        countryDomainMap.put("am", "Armenia");
        countryDomainMap.put("an", "Netherlands Antilles");
        countryDomainMap.put("ao", "Angola");
        countryDomainMap.put("aq", "Antarctica");
        countryDomainMap.put("ar", "Argentina");
        countryDomainMap.put("as", "American Samoa");
        countryDomainMap.put("at", "Austria");
        countryDomainMap.put("au", "Australia");
        countryDomainMap.put("aw", "Aruba");
        countryDomainMap.put("az", "Azerbaijan");
        countryDomainMap.put("ba", "Bosnia and Herzegovina");
        countryDomainMap.put("bb", "Barbados");
        countryDomainMap.put("bd", "Bangladesh");
        countryDomainMap.put("be", "Belgium");
        countryDomainMap.put("bf", "Burkina Faso");
        countryDomainMap.put("bg", "Bulgaria");
        countryDomainMap.put("bh", "Bahrain");
        countryDomainMap.put("bi", "Burundi");
        countryDomainMap.put("bj", "Benin");
        countryDomainMap.put("bm", "Bermuda");
        countryDomainMap.put("bn", "Brunei Darussalam");
        countryDomainMap.put("bo", "Bolivia");
        countryDomainMap.put("br", "Brazil");
        countryDomainMap.put("bs", "Bahamas");
        countryDomainMap.put("bt", "Bhutan");
        countryDomainMap.put("bv", "Bouvet Island");
        countryDomainMap.put("bw", "Botswana");
        countryDomainMap.put("by", "Belarus");
        countryDomainMap.put("bz", "Belize");
        countryDomainMap.put("ca", "Canada");
        countryDomainMap.put("cc", "Cocos (Keeling) Islands");
        countryDomainMap.put("cf", "Central African Republic");
        countryDomainMap.put("cg", "Congo");
        countryDomainMap.put("ch", "Switzerland");
        countryDomainMap.put("ci", "Cote D'Ivoire (Ivory Coast)");
        countryDomainMap.put("ck", "Cook Islands");
        countryDomainMap.put("cl", "Chile");
        countryDomainMap.put("cm", "Cameroon");
        countryDomainMap.put("cn", "China");
        countryDomainMap.put("co", "Colombia");
        countryDomainMap.put("cr", "Costa Rica");
        countryDomainMap.put("cs", "Czechoslovakia (former)");
        countryDomainMap.put("cu", "Cuba");
        countryDomainMap.put("cv", "Cape Verde");
        countryDomainMap.put("cx", "Christmas Island");
        countryDomainMap.put("cy", "Cyprus");
        countryDomainMap.put("cz", "Czech Republic");
        countryDomainMap.put("de", "Germany");
        countryDomainMap.put("dj", "Djibouti");
        countryDomainMap.put("dk", "Denmark");
        countryDomainMap.put("dm", "Dominica");
        countryDomainMap.put("do", "Dominican Republic");
        countryDomainMap.put("dz", "Algeria");
        countryDomainMap.put("ec", "Ecuador");
        countryDomainMap.put("ee", "Estonia");
        countryDomainMap.put("eg", "Egypt");
        countryDomainMap.put("eh", "Western Sahara");
        countryDomainMap.put("er", "Eritrea");
        countryDomainMap.put("es", "Spain");
        countryDomainMap.put("et", "Ethiopia");
        countryDomainMap.put("fi", "Finland");
        countryDomainMap.put("fj", "Fiji");
        countryDomainMap.put("fk", "Falkland Islands (Malvinas)");
        countryDomainMap.put("fm", "Micronesia");
        countryDomainMap.put("fo", "Faroe Islands");
        countryDomainMap.put("fr", "France");
        countryDomainMap.put("fx", "France, Metropolitan");
        countryDomainMap.put("ga", "Gabon");
        countryDomainMap.put("gb", "Great Britain (UK)");
        countryDomainMap.put("gd", "Grenada");
        countryDomainMap.put("ge", "Georgia");
        countryDomainMap.put("gf", "French Guiana");
        countryDomainMap.put("gh", "Ghana");
        countryDomainMap.put("gi", "Gibraltar");
        countryDomainMap.put("gl", "Greenland");
        countryDomainMap.put("gm", "Gambia");
        countryDomainMap.put("gn", "Guinea");
        countryDomainMap.put("gp", "Guadeloupe");
        countryDomainMap.put("gq", "Equatorial Guinea");
        countryDomainMap.put("gr", "Greece");
        countryDomainMap.put("gs", "S. Georgia and S. Sandwich Isls.");
        countryDomainMap.put("gt", "Guatemala");
        countryDomainMap.put("gu", "Guam");
        countryDomainMap.put("gw", "Guinea-Bissau");
        countryDomainMap.put("gy", "Guyana");
        countryDomainMap.put("hk", "Hong Kong");
        countryDomainMap.put("hm", "Heard and McDonald Islands");
        countryDomainMap.put("hn", "Honduras");
        countryDomainMap.put("hr", "Croatia (Hrvatska)");
        countryDomainMap.put("ht", "Haiti");
        countryDomainMap.put("hu", "Hungary");
        countryDomainMap.put("id", "Indonesia");
        countryDomainMap.put("ie", "Ireland");
        countryDomainMap.put("il", "Israel");
        countryDomainMap.put("in", "India");
        countryDomainMap.put("io", "British Indian Ocean Territory");
        countryDomainMap.put("iq", "Iraq");
        countryDomainMap.put("ir", "Iran");
        countryDomainMap.put("is", "Iceland");
        countryDomainMap.put("it", "Italy");
        countryDomainMap.put("jm", "Jamaica");
        countryDomainMap.put("jo", "Jordan");
        countryDomainMap.put("jp", "Japan");
        countryDomainMap.put("ke", "Kenya");
        countryDomainMap.put("kg", "Kyrgyzstan");
        countryDomainMap.put("kh", "Cambodia");
        countryDomainMap.put("ki", "Kiribati");
        countryDomainMap.put("km", "Comoros");
        countryDomainMap.put("kn", "Saint Kitts and Nevis");
        countryDomainMap.put("kp", "Korea (North)");
        countryDomainMap.put("kr", "Korea (South)");
        countryDomainMap.put("kw", "Kuwait");
        countryDomainMap.put("ky", "Cayman Islands");
        countryDomainMap.put("kz", "Kazakhstan");
        countryDomainMap.put("la", "Laos");
        countryDomainMap.put("lb", "Lebanon");
        countryDomainMap.put("lc", "Saint Lucia");
        countryDomainMap.put("li", "Liechtenstein");
        countryDomainMap.put("lk", "Sri Lanka");
        countryDomainMap.put("lr", "Liberia");
        countryDomainMap.put("ls", "Lesotho");
        countryDomainMap.put("lt", "Lithuania");
        countryDomainMap.put("lu", "Luxembourg");
        countryDomainMap.put("lv", "Latvia");
        countryDomainMap.put("ly", "Libya");
        countryDomainMap.put("ma", "Morocco");
        countryDomainMap.put("mc", "Monaco");
        countryDomainMap.put("md", "Moldova");
        countryDomainMap.put("me", "Me");
        countryDomainMap.put("mg", "Madagascar");
        countryDomainMap.put("mh", "Marshall Islands");
        countryDomainMap.put("mk", "Macedonia");
        countryDomainMap.put("ml", "Mali");
        countryDomainMap.put("mm", "Myanmar");
        countryDomainMap.put("mn", "Mongolia");
        countryDomainMap.put("mo", "Macau");
        countryDomainMap.put("mp", "Northern Mariana Islands");
        countryDomainMap.put("mq", "Martinique");
        countryDomainMap.put("mr", "Mauritania");
        countryDomainMap.put("ms", "Montserrat");
        countryDomainMap.put("mt", "Malta");
        countryDomainMap.put("mu", "Mauritius");
        countryDomainMap.put("mv", "Maldives");
        countryDomainMap.put("mw", "Malawi");
        countryDomainMap.put("mx", "Mexico");
        countryDomainMap.put("my", "Malaysia");
        countryDomainMap.put("mz", "Mozambique");
        countryDomainMap.put("na", "Namibia");
        countryDomainMap.put("nc", "New Caledonia");
        countryDomainMap.put("ne", "Niger");
        countryDomainMap.put("nf", "Norfolk Island");
        countryDomainMap.put("ng", "Nigeria");
        countryDomainMap.put("ni", "Nicaragua");
        countryDomainMap.put("nl", "Netherlands");
        countryDomainMap.put("no", "Norway");
        countryDomainMap.put("np", "Nepal");
        countryDomainMap.put("nr", "Nauru");
        countryDomainMap.put("nt", "Neutral Zone");
        countryDomainMap.put("nu", "Niue");
        countryDomainMap.put("nz", "New Zealand (Aotearoa)");
        countryDomainMap.put("om", "Oman");
        countryDomainMap.put("pa", "Panama");
        countryDomainMap.put("pe", "Peru");
        countryDomainMap.put("pf", "French Polynesia");
        countryDomainMap.put("pg", "Papua New Guinea");
        countryDomainMap.put("ph", "Philippines");
        countryDomainMap.put("pk", "Pakistan");
        countryDomainMap.put("pl", "Poland");
        countryDomainMap.put("pm", "St. Pierre and Miquelon");
        countryDomainMap.put("pn", "Pitcairn");
        countryDomainMap.put("pr", "Puerto Rico");
        countryDomainMap.put("pt", "Portugal");
        countryDomainMap.put("pw", "Palau");
        countryDomainMap.put("py", "Paraguay");
        countryDomainMap.put("qa", "Qatar");
        countryDomainMap.put("re", "Reunion");
        countryDomainMap.put("ro", "Romania");
        countryDomainMap.put("ru", "Russian Federation");
        countryDomainMap.put("rw", "Rwanda");
        countryDomainMap.put("sa", "Saudi Arabia");
        countryDomainMap.put("sb", "Solomon Islands");
        countryDomainMap.put("sc", "Seychelles");
        countryDomainMap.put("sd", "Sudan");
        countryDomainMap.put("se", "Sweden");
        countryDomainMap.put("sg", "Singapore");
        countryDomainMap.put("sh", "St. Helena");
        countryDomainMap.put("si", "Slovenia");
        countryDomainMap.put("sj", "Svalbard and Jan Mayen Islands");
        countryDomainMap.put("sk", "Slovak Republic");
        countryDomainMap.put("sl", "Sierra Leone");
        countryDomainMap.put("sm", "San Marino");
        countryDomainMap.put("sn", "Senegal");
        countryDomainMap.put("so", "Somalia");
        countryDomainMap.put("sr", "Suriname");
        countryDomainMap.put("st", "Sao Tome and Principe");
        countryDomainMap.put("su", "USSR (former)");
        countryDomainMap.put("sv", "El Salvador");
        countryDomainMap.put("sy", "Syria");
        countryDomainMap.put("sz", "Swaziland");
        countryDomainMap.put("tc", "Turks and Caicos Islands");
        countryDomainMap.put("tel", "tel");
        countryDomainMap.put("td", "Chad");
        countryDomainMap.put("tf", "French Southern Territories");
        countryDomainMap.put("tg", "Togo");
        countryDomainMap.put("th", "Thailand");
        countryDomainMap.put("tj", "Tajikistan");
        countryDomainMap.put("tk", "Tokelau");
        countryDomainMap.put("tm", "Turkmenistan");
        countryDomainMap.put("tn", "Tunisia");
        countryDomainMap.put("to", "Tonga");
        countryDomainMap.put("tp", "East Timor");
        countryDomainMap.put("tr", "Turkey");
        countryDomainMap.put("tt", "Trinidad and Tobago");
        countryDomainMap.put("tv", "Tuvalu");
        countryDomainMap.put("tw", "Taiwan");
        countryDomainMap.put("tz", "Tanzania");
        countryDomainMap.put("ua", "Ukraine");
        countryDomainMap.put("ug", "Uganda");
        countryDomainMap.put("uk", "United Kingdom");
        countryDomainMap.put("um", "US Minor Outlying Islands");
        countryDomainMap.put("us", "United States");
        countryDomainMap.put("uy", "Uruguay");
        countryDomainMap.put("uz", "Uzbekistan");
        countryDomainMap.put("va", "Vatican City State (Holy See)");
        countryDomainMap.put("vc", "Saint Vincent and the Grenadines");
        countryDomainMap.put("ve", "Venezuela");
        countryDomainMap.put("vg", "Virgin Islands (British)");
        countryDomainMap.put("vi", "Virgin Islands (U.S.)");
        countryDomainMap.put("vn", "Viet Nam");
        countryDomainMap.put("vu", "Vanuatu");
        countryDomainMap.put("wf", "Wallis and Futuna Islands");
        countryDomainMap.put("ws", "Samoa");
        countryDomainMap.put("ye", "Yemen");
        countryDomainMap.put("yt", "Mayotte");
        countryDomainMap.put("yu", "Yugoslavia");
        countryDomainMap.put("za", "South Africa");
        countryDomainMap.put("zm", "Zambia");
        countryDomainMap.put("zr", "Zaire");
        countryDomainMap.put("zw", "Zimbabwe");
        
        // add chinese name domain tail
        countryDomainMap.put("\u4e2d\u56fd", "chinese domain");
    }

    /**
     * init func domain map 
     */
    private void initFuncDomainMap() {
        funcDomainMap.put("co", "Commercial");
        funcDomainMap.put("ac", "Academic?");
        funcDomainMap.put("or", "short for org?");
        funcDomainMap.put("ne", "short for net?");
        funcDomainMap.put("ad", "Academic?");
        funcDomainMap.put("com", "Commercial");
        funcDomainMap.put("edu", "Educational");
        funcDomainMap.put("gov", "Government");
        funcDomainMap.put("int", "International");
        funcDomainMap.put("mil", "US Military");
        funcDomainMap.put("net", "Network");
        funcDomainMap.put("org", "Non-Profit Organization");
        funcDomainMap.put("biz", "business");
        funcDomainMap.put("arpa", "Old style Arpanet");
        funcDomainMap.put("nato", "Nato field");
        funcDomainMap.put("info", "info");        
        funcDomainMap.put("mobi", "mobile");       
        funcDomainMap.put("asia", "asia");
    }
    
    /**
     * init domain tail map
     */
    private void initDomainTailMap() {
        domainTailMap.put("busan.kr", 1);
        domainTailMap.put("chungbuk.kr", 1);
        domainTailMap.put("chungnam.kr", 1);
        domainTailMap.put("co.ag", 1);
        domainTailMap.put("com.ag", 1);
        domainTailMap.put("conf.au", 1);
        domainTailMap.put("csiro.au", 1);
        domainTailMap.put("daegu.kr", 1);
        domainTailMap.put("daejeon.kr", 1);
        domainTailMap.put("dts.in", 1);
        domainTailMap.put("edu.sg", 1);
        domainTailMap.put("ernet.in", 1);
        domainTailMap.put("esc.edu.ar", 1);
        domainTailMap.put("firm.in", 1);
        domainTailMap.put("gangwon.kr", 1);
        domainTailMap.put("gen.in", 1);
        domainTailMap.put("go.th", 1);
        domainTailMap.put("gwangju.kr", 1);
        domainTailMap.put("gyeongbuk.kr", 1);
        domainTailMap.put("gyeonggi.kr", 1);
        domainTailMap.put("gyeongnam.kr", 1);
        domainTailMap.put("incheon.kr", 1);
        domainTailMap.put("ind.in", 1);
        domainTailMap.put("int.ar", 1);
        domainTailMap.put("jeju.kr", 1);
        domainTailMap.put("jeonbuk.kr", 1);
        domainTailMap.put("jeonnam.kr", 1);
        domainTailMap.put("ltd.uk", 1);
        domainTailMap.put("me.uk", 1);
        domainTailMap.put("ne.at", 1);
        domainTailMap.put("nom.ag", 1);
        domainTailMap.put("or.at", 1);
        domainTailMap.put("or.th", 1);
        domainTailMap.put("plc.uk", 1);
        domainTailMap.put("pr.kr", 1);
        domainTailMap.put("res.in", 1);
        domainTailMap.put("sch.edu.sg", 1);
        domainTailMap.put("seoul.kr", 1);
        domainTailMap.put("telememo.au", 1);
        domainTailMap.put("ulsan.kr", 1);
        domainTailMap.put("ad.jp", 1);
        domainTailMap.put("ak.us", 1);
        domainTailMap.put("al.us", 1);
        domainTailMap.put("ar.us", 1);
        domainTailMap.put("asn.au", 1);
        domainTailMap.put("az.us", 1);
        domainTailMap.put("bc.ca", 1);
        domainTailMap.put("busan.kr", 1);
        domainTailMap.put("ca.us", 1);
        domainTailMap.put("cc.ca.us", 1);
        domainTailMap.put("cc.fl.us", 1);
        domainTailMap.put("cc.il.us", 1);
        domainTailMap.put("cc.md.us", 1);
        domainTailMap.put("cc.mi.us", 1);
        domainTailMap.put("cc.nc.us", 1);
        domainTailMap.put("cc.tx.us", 1);
        domainTailMap.put("cc.va.us", 1);
        domainTailMap.put("chungbuk.kr", 1);
        domainTailMap.put("chungnam.kr", 1);
        domainTailMap.put("conf.au", 1);
        domainTailMap.put("csiro.au", 1);
        domainTailMap.put("ct.us", 1);
        domainTailMap.put("daegu.kr", 1);
        domainTailMap.put("daejeon.kr", 1);
        domainTailMap.put("dc.us", 1);
        domainTailMap.put("de.us", 1);
        domainTailMap.put("dts.in", 1);
        domainTailMap.put("ed.jp", 1);
        domainTailMap.put("ernet.in", 1);
        domainTailMap.put("esc.edu.ar", 1);
        domainTailMap.put("eu.int", 1);
        domainTailMap.put("fed.us", 1);
        domainTailMap.put("firm.in", 1);
        domainTailMap.put("fl.us", 1);
        domainTailMap.put("fs.fed.us", 1);
        domainTailMap.put("ga.us", 1);
        domainTailMap.put("gangwon.kr", 1);
        domainTailMap.put("gen.in", 1);
        domainTailMap.put("geo.jp", 1);
        domainTailMap.put("go.jp", 1);
        domainTailMap.put("go.kr", 1);
        domainTailMap.put("go.th", 1);
        domainTailMap.put("gr.jp", 1);
        domainTailMap.put("gwangju.kr", 1);
        domainTailMap.put("gyeongbuk.kr", 1);
        domainTailMap.put("gyeonggi.kr", 1);
        domainTailMap.put("gyeongnam.kr", 1);
        domainTailMap.put("hi.us", 1);
        domainTailMap.put("ia.us", 1);
        domainTailMap.put("id.au", 1);
        domainTailMap.put("id.us", 1);
        domainTailMap.put("il.us", 1);
        domainTailMap.put("in.us", 1);
        domainTailMap.put("incheon.kr", 1);
        domainTailMap.put("ind.in", 1);
        domainTailMap.put("info.au", 1);
        domainTailMap.put("int.ar", 1);
        domainTailMap.put("jeju.kr", 1);
        domainTailMap.put("jeonbuk.kr", 1);
        domainTailMap.put("jeonnam.kr", 1);
        domainTailMap.put("k12.ak.us", 1);
        domainTailMap.put("k12.al.us", 1);
        domainTailMap.put("k12.ar.us", 1);
        domainTailMap.put("k12.az.us", 1);
        domainTailMap.put("k12.ca.us", 1);
        domainTailMap.put("k12.co.us", 1);
        domainTailMap.put("k12.ct.us", 1);
        domainTailMap.put("k12.fl.us", 1);
        domainTailMap.put("k12.ga.us", 1);
        domainTailMap.put("k12.hi.us", 1);
        domainTailMap.put("k12.ia.us", 1);
        domainTailMap.put("k12.id.us", 1);
        domainTailMap.put("k12.il.us", 1);
        domainTailMap.put("k12.in.us", 1);
        domainTailMap.put("k12.ks.us", 1);
        domainTailMap.put("k12.ky.us", 1);
        domainTailMap.put("k12.la.us", 1);
        domainTailMap.put("k12.ma.us", 1);
        domainTailMap.put("k12.md.us", 1);
        domainTailMap.put("k12.me.us", 1);
        domainTailMap.put("k12.mi.us", 1);
        domainTailMap.put("k12.mn.us", 1);
        domainTailMap.put("k12.mo.us", 1);
        domainTailMap.put("k12.mt.us", 1);
        domainTailMap.put("k12.nc.us", 1);
        domainTailMap.put("k12.nd.us", 1);
        domainTailMap.put("k12.ne.us", 1);
        domainTailMap.put("k12.nh.us", 1);
        domainTailMap.put("k12.nj.us", 1);
        domainTailMap.put("k12.nm.us", 1);
        domainTailMap.put("k12.ny.us", 1);
        domainTailMap.put("k12.oh.us", 1);
        domainTailMap.put("k12.ok.us", 1);
        domainTailMap.put("k12.or.us", 1);
        domainTailMap.put("k12.pa.us", 1);
        domainTailMap.put("k12.sc.us", 1);
        domainTailMap.put("k12.sd.us", 1);
        domainTailMap.put("k12.tn.us", 1);
        domainTailMap.put("k12.tx.us", 1);
        domainTailMap.put("k12.ut.us", 1);
        domainTailMap.put("k12.va.us", 1);
        domainTailMap.put("k12.vt.us", 1);
        domainTailMap.put("k12.wa.us", 1);
        domainTailMap.put("k12.wi.us", 1);
        domainTailMap.put("k12.wv.us", 1);
        domainTailMap.put("k12.wy.us", 1);
        domainTailMap.put("kiev.ua", 1);
        domainTailMap.put("ks.us", 1);
        domainTailMap.put("ky.us", 1);
        domainTailMap.put("la.ca.us", 1);
        domainTailMap.put("la.us", 1);
        domainTailMap.put("lib.il.us", 1);
        domainTailMap.put("lib.in.us", 1);
        domainTailMap.put("lib.mi.us", 1);
        domainTailMap.put("lib.ny.us", 1);
        domainTailMap.put("lib.oh.us", 1);
        domainTailMap.put("ltd.uk", 1);
        domainTailMap.put("ma.us", 1);
        domainTailMap.put("md.us", 1);
        domainTailMap.put("me.uk", 1);
        domainTailMap.put("me.us", 1);
        domainTailMap.put("mi.us", 1);
        domainTailMap.put("mn.us", 1);
        domainTailMap.put("mo.us", 1);
        domainTailMap.put("ms.us", 1);
        domainTailMap.put("mt.us", 1);
        domainTailMap.put("nc.us", 1);
        domainTailMap.put("nd.us", 1);
        domainTailMap.put("ne.at", 1);
        domainTailMap.put("ne.jp", 1);
        domainTailMap.put("ne.kr", 1);
        domainTailMap.put("ne.us", 1);
        domainTailMap.put("nh.us", 1);
        domainTailMap.put("nj.us", 1);
        domainTailMap.put("nm.us", 1);
        domainTailMap.put("nom.ag", 1);
        domainTailMap.put("nv.us", 1);
        domainTailMap.put("ny.us", 1);
        domainTailMap.put("oh.us", 1);
        domainTailMap.put("ok.us", 1);
        domainTailMap.put("on.ca", 1);
        domainTailMap.put("or.at", 1);
        domainTailMap.put("or.ir", 1);
        domainTailMap.put("or.jp", 1);
        domainTailMap.put("or.ke", 1);
        domainTailMap.put("or.kr", 1);
        domainTailMap.put("or.th", 1);
        domainTailMap.put("or.ug", 1);
        domainTailMap.put("or.us", 1);
        domainTailMap.put("oz.au", 1);
        domainTailMap.put("pa.us", 1);
        domainTailMap.put("pe.kr", 1);
        domainTailMap.put("plc.uk", 1);
        domainTailMap.put("pr.kr", 1);
        domainTailMap.put("qc.ca", 1);
        domainTailMap.put("re.kr", 1);
        domainTailMap.put("res.in", 1);
        domainTailMap.put("ri.us", 1);
        domainTailMap.put("sc.us", 1);
        domainTailMap.put("sch.edu.sg", 1);
        domainTailMap.put("sch.uk", 1);
        domainTailMap.put("sd.us", 1);
        domainTailMap.put("seoul.kr", 1);
        domainTailMap.put("state.ak.us", 1);
        domainTailMap.put("state.al.us", 1);
        domainTailMap.put("state.az.us", 1);
        domainTailMap.put("state.co.us", 1);
        domainTailMap.put("state.fl.us", 1);
        domainTailMap.put("state.ga.us", 1);
        domainTailMap.put("state.il.us", 1);
        domainTailMap.put("state.ky.us", 1);
        domainTailMap.put("state.la.us", 1);
        domainTailMap.put("state.md.us", 1);
        domainTailMap.put("state.mi.us", 1);
        domainTailMap.put("state.mn.us", 1);
        domainTailMap.put("state.ms.us", 1);
        domainTailMap.put("state.mt.us", 1);
        domainTailMap.put("state.nc.us", 1);
        domainTailMap.put("state.ne.us", 1);
        domainTailMap.put("state.ny.us", 1);
        domainTailMap.put("state.oh.us", 1);
        domainTailMap.put("state.or.us", 1);
        domainTailMap.put("state.pa.us", 1);
        domainTailMap.put("state.tx.us", 1);
        domainTailMap.put("state.ut.us", 1);
        domainTailMap.put("state.va.us", 1);
        domainTailMap.put("state.wi.us", 1);
        domainTailMap.put("tec.tn.us", 1);
        domainTailMap.put("tec.wi.us", 1);
        domainTailMap.put("telememo.au", 1);
        domainTailMap.put("tn.us", 1);
        domainTailMap.put("tx.us", 1);
        domainTailMap.put("ulsan.kr", 1);
        domainTailMap.put("ut.us", 1);
        domainTailMap.put("va.us", 1);
        domainTailMap.put("vt.us", 1);
        domainTailMap.put("wa.us", 1);
        domainTailMap.put("wi.us", 1);
        domainTailMap.put("wv.us", 1);
        domainTailMap.put("wy.us", 1);
        domainTailMap.put("com.com", 1);
        domainTailMap.put("com.ne.kr", 1);
        domainTailMap.put("ac.net", 1);
        domainTailMap.put("ac.com.au", 1);
        domainTailMap.put("biz.net", 1);
        domainTailMap.put("co.net", 1);
        domainTailMap.put("name", 1);
    }
    
    /**
     * generate domain tail map 
     */
    private void generateDomainTailMap() {
        for(String country:countryDomainMap.keySet()) {
            domainTailMap.put(country, 1);
            for(String func:funcDomainMap.keySet()) {
                String domainTail = func + "." + country;
                domainTailMap.put(func, 1);
                domainTailMap.put(domainTail, 1);
            }
        }
    }
    
}
