
package webServices.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "KVBServerService", targetNamespace = "http://webServices/", wsdlLocation = "http://localhost:8080/kvWS/?wsdl")
public class KVBServerService
    extends Service
{

    private final static URL KVBSERVERSERVICE_WSDL_LOCATION;
    private final static WebServiceException KVBSERVERSERVICE_EXCEPTION;
    private final static QName KVBSERVERSERVICE_QNAME = new QName("http://webServices/", "KVBServerService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/kvWS/?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        KVBSERVERSERVICE_WSDL_LOCATION = url;
        KVBSERVERSERVICE_EXCEPTION = e;
    }

    public KVBServerService() {
        super(__getWsdlLocation(), KVBSERVERSERVICE_QNAME);
    }

    public KVBServerService(WebServiceFeature... features) {
        super(__getWsdlLocation(), KVBSERVERSERVICE_QNAME, features);
    }

    public KVBServerService(URL wsdlLocation) {
        super(wsdlLocation, KVBSERVERSERVICE_QNAME);
    }

    public KVBServerService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, KVBSERVERSERVICE_QNAME, features);
    }

    public KVBServerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public KVBServerService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns KVBServer
     */
    @WebEndpoint(name = "KVBServerPort")
    public KVBServer getKVBServerPort() {
        return super.getPort(new QName("http://webServices/", "KVBServerPort"), KVBServer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns KVBServer
     */
    @WebEndpoint(name = "KVBServerPort")
    public KVBServer getKVBServerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webServices/", "KVBServerPort"), KVBServer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (KVBSERVERSERVICE_EXCEPTION!= null) {
            throw KVBSERVERSERVICE_EXCEPTION;
        }
        return KVBSERVERSERVICE_WSDL_LOCATION;
    }

}
