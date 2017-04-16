package com.transformice.helpers.network;

import com.maxmind.geoip.LookupService;
import com.transformice.logging.Logging;

public class GeoIP {

    private LookupService lookup;

    public GeoIP() throws Exception {
        long start = System.currentTimeMillis();
        this.lookup = new LookupService("./data/GeoIP.dat", LookupService.GEOIP_MEMORY_CACHE);
        Logging.print("GeoIP: Loaded in : " + (System.currentTimeMillis() - start) + "ms.", "info");
    }

    public String getCountryCode(String ipAddress) {
        if (this.lookup != null) {
            switch(this.lookup.getCountry(ipAddress).getCode()) {
                case "BR":
                    return "BR";
                case "PT":
                    return "PT";
                case "VE":
                    return "ES";
                case "UY":
                    return "ES";
                case "DO":
                case "PE":
                case "PA":
                case "PY":
                case "NI":
                case "HN":
                case "GT":
                case "SV":
                case "CU":
                case "CR":
                case "CO":
                    return "ES";
                case "CL":
                case "BO":
                case "AR":
                    return "ES";
                case "MX":
                    return "ES";
                case "TR":
                case "TM":
                case "TC":
                case "BG":
                    return "TR";
            }
        }
        return "EN";
    }
}
