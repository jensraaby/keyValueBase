
package webServices.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the webServices.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UpdateResponse_QNAME = new QName("http://webServices/", "updateResponse");
    private final static QName _ValueListImpl_QNAME = new QName("http://webServices/", "valueListImpl");
    private final static QName _DeleteResponse_QNAME = new QName("http://webServices/", "deleteResponse");
    private final static QName _Insert_QNAME = new QName("http://webServices/", "insert");
    private final static QName _Delete_QNAME = new QName("http://webServices/", "delete");
    private final static QName _FileNotFoundException_QNAME = new QName("http://webServices/", "FileNotFoundException");
    private final static QName _Read_QNAME = new QName("http://webServices/", "read");
    private final static QName _KeyImpl_QNAME = new QName("http://webServices/", "keyImpl");
    private final static QName _ReadResponse_QNAME = new QName("http://webServices/", "readResponse");
    private final static QName _Scan_QNAME = new QName("http://webServices/", "scan");
    private final static QName _ScanResponse_QNAME = new QName("http://webServices/", "scanResponse");
    private final static QName _IOException_QNAME = new QName("http://webServices/", "IOException");
    private final static QName _KeyAlreadyPresentException_QNAME = new QName("http://webServices/", "KeyAlreadyPresentException");
    private final static QName _ServiceNotInitializedException_QNAME = new QName("http://webServices/", "ServiceNotInitializedException");
    private final static QName _KeyNotFoundException_QNAME = new QName("http://webServices/", "KeyNotFoundException");
    private final static QName _Update_QNAME = new QName("http://webServices/", "update");
    private final static QName _BeginGreaterThanEndException_QNAME = new QName("http://webServices/", "BeginGreaterThanEndException");
    private final static QName _InitResponse_QNAME = new QName("http://webServices/", "initResponse");
    private final static QName _ValueImpl_QNAME = new QName("http://webServices/", "valueImpl");
    private final static QName _Init_QNAME = new QName("http://webServices/", "init");
    private final static QName _InsertResponse_QNAME = new QName("http://webServices/", "insertResponse");
    private final static QName _ServiceInitializingException_QNAME = new QName("http://webServices/", "ServiceInitializingException");
    private final static QName _ServiceAlreadyInitializedException_QNAME = new QName("http://webServices/", "ServiceAlreadyInitializedException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webServices.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Update }
     * 
     */
    public Update createUpdate() {
        return new Update();
    }

    /**
     * Create an instance of {@link ScanResponse }
     * 
     */
    public ScanResponse createScanResponse() {
        return new ScanResponse();
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link KeyAlreadyPresentException }
     * 
     */
    public KeyAlreadyPresentException createKeyAlreadyPresentException() {
        return new KeyAlreadyPresentException();
    }

    /**
     * Create an instance of {@link ServiceNotInitializedException }
     * 
     */
    public ServiceNotInitializedException createServiceNotInitializedException() {
        return new ServiceNotInitializedException();
    }

    /**
     * Create an instance of {@link KeyNotFoundException }
     * 
     */
    public KeyNotFoundException createKeyNotFoundException() {
        return new KeyNotFoundException();
    }

    /**
     * Create an instance of {@link ValueImpl }
     * 
     */
    public ValueImpl createValueImpl() {
        return new ValueImpl();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link InsertResponse }
     * 
     */
    public InsertResponse createInsertResponse() {
        return new InsertResponse();
    }

    /**
     * Create an instance of {@link ServiceInitializingException }
     * 
     */
    public ServiceInitializingException createServiceInitializingException() {
        return new ServiceInitializingException();
    }

    /**
     * Create an instance of {@link ServiceAlreadyInitializedException }
     * 
     */
    public ServiceAlreadyInitializedException createServiceAlreadyInitializedException() {
        return new ServiceAlreadyInitializedException();
    }

    /**
     * Create an instance of {@link BeginGreaterThanEndException }
     * 
     */
    public BeginGreaterThanEndException createBeginGreaterThanEndException() {
        return new BeginGreaterThanEndException();
    }

    /**
     * Create an instance of {@link InitResponse }
     * 
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link DeleteResponse }
     * 
     */
    public DeleteResponse createDeleteResponse() {
        return new DeleteResponse();
    }

    /**
     * Create an instance of {@link ValueListImpl }
     * 
     */
    public ValueListImpl createValueListImpl() {
        return new ValueListImpl();
    }

    /**
     * Create an instance of {@link UpdateResponse }
     * 
     */
    public UpdateResponse createUpdateResponse() {
        return new UpdateResponse();
    }

    /**
     * Create an instance of {@link FileNotFoundException }
     * 
     */
    public FileNotFoundException createFileNotFoundException() {
        return new FileNotFoundException();
    }

    /**
     * Create an instance of {@link Delete }
     * 
     */
    public Delete createDelete() {
        return new Delete();
    }

    /**
     * Create an instance of {@link Read }
     * 
     */
    public Read createRead() {
        return new Read();
    }

    /**
     * Create an instance of {@link Scan }
     * 
     */
    public Scan createScan() {
        return new Scan();
    }

    /**
     * Create an instance of {@link ReadResponse }
     * 
     */
    public ReadResponse createReadResponse() {
        return new ReadResponse();
    }

    /**
     * Create an instance of {@link KeyImpl }
     * 
     */
    public KeyImpl createKeyImpl() {
        return new KeyImpl();
    }

    /**
     * Create an instance of {@link Insert }
     * 
     */
    public Insert createInsert() {
        return new Insert();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "updateResponse")
    public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
        return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueListImpl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "valueListImpl")
    public JAXBElement<ValueListImpl> createValueListImpl(ValueListImpl value) {
        return new JAXBElement<ValueListImpl>(_ValueListImpl_QNAME, ValueListImpl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "deleteResponse")
    public JAXBElement<DeleteResponse> createDeleteResponse(DeleteResponse value) {
        return new JAXBElement<DeleteResponse>(_DeleteResponse_QNAME, DeleteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Insert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "insert")
    public JAXBElement<Insert> createInsert(Insert value) {
        return new JAXBElement<Insert>(_Insert_QNAME, Insert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Delete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "delete")
    public JAXBElement<Delete> createDelete(Delete value) {
        return new JAXBElement<Delete>(_Delete_QNAME, Delete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "FileNotFoundException")
    public JAXBElement<FileNotFoundException> createFileNotFoundException(FileNotFoundException value) {
        return new JAXBElement<FileNotFoundException>(_FileNotFoundException_QNAME, FileNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Read }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "read")
    public JAXBElement<Read> createRead(Read value) {
        return new JAXBElement<Read>(_Read_QNAME, Read.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyImpl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "keyImpl")
    public JAXBElement<KeyImpl> createKeyImpl(KeyImpl value) {
        return new JAXBElement<KeyImpl>(_KeyImpl_QNAME, KeyImpl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "readResponse")
    public JAXBElement<ReadResponse> createReadResponse(ReadResponse value) {
        return new JAXBElement<ReadResponse>(_ReadResponse_QNAME, ReadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Scan }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "scan")
    public JAXBElement<Scan> createScan(Scan value) {
        return new JAXBElement<Scan>(_Scan_QNAME, Scan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScanResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "scanResponse")
    public JAXBElement<ScanResponse> createScanResponse(ScanResponse value) {
        return new JAXBElement<ScanResponse>(_ScanResponse_QNAME, ScanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyAlreadyPresentException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "KeyAlreadyPresentException")
    public JAXBElement<KeyAlreadyPresentException> createKeyAlreadyPresentException(KeyAlreadyPresentException value) {
        return new JAXBElement<KeyAlreadyPresentException>(_KeyAlreadyPresentException_QNAME, KeyAlreadyPresentException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceNotInitializedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "ServiceNotInitializedException")
    public JAXBElement<ServiceNotInitializedException> createServiceNotInitializedException(ServiceNotInitializedException value) {
        return new JAXBElement<ServiceNotInitializedException>(_ServiceNotInitializedException_QNAME, ServiceNotInitializedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "KeyNotFoundException")
    public JAXBElement<KeyNotFoundException> createKeyNotFoundException(KeyNotFoundException value) {
        return new JAXBElement<KeyNotFoundException>(_KeyNotFoundException_QNAME, KeyNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Update }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "update")
    public JAXBElement<Update> createUpdate(Update value) {
        return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BeginGreaterThanEndException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "BeginGreaterThanEndException")
    public JAXBElement<BeginGreaterThanEndException> createBeginGreaterThanEndException(BeginGreaterThanEndException value) {
        return new JAXBElement<BeginGreaterThanEndException>(_BeginGreaterThanEndException_QNAME, BeginGreaterThanEndException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "initResponse")
    public JAXBElement<InitResponse> createInitResponse(InitResponse value) {
        return new JAXBElement<InitResponse>(_InitResponse_QNAME, InitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValueImpl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "valueImpl")
    public JAXBElement<ValueImpl> createValueImpl(ValueImpl value) {
        return new JAXBElement<ValueImpl>(_ValueImpl_QNAME, ValueImpl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InsertResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "insertResponse")
    public JAXBElement<InsertResponse> createInsertResponse(InsertResponse value) {
        return new JAXBElement<InsertResponse>(_InsertResponse_QNAME, InsertResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceInitializingException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "ServiceInitializingException")
    public JAXBElement<ServiceInitializingException> createServiceInitializingException(ServiceInitializingException value) {
        return new JAXBElement<ServiceInitializingException>(_ServiceInitializingException_QNAME, ServiceInitializingException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceAlreadyInitializedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "ServiceAlreadyInitializedException")
    public JAXBElement<ServiceAlreadyInitializedException> createServiceAlreadyInitializedException(ServiceAlreadyInitializedException value) {
        return new JAXBElement<ServiceAlreadyInitializedException>(_ServiceAlreadyInitializedException_QNAME, ServiceAlreadyInitializedException.class, null, value);
    }

}
