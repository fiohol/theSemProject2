
package org.thesemproject.server;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SemService", targetNamespace = "http://server.thesemproject.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SemService {


    /**
     * 
     * @param server
     * @param text
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getLanguage", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetLanguage")
    @ResponseWrapper(localName = "getLanguageResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetLanguageResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getLanguageRequest", output = "http://server.thesemproject.org/SemService/getLanguageResponse")
    public String getLanguage(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "text", targetNamespace = "")
        String text);

    /**
     * 
     * @param server
     * @param text
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getHtmlSegmentationFromText", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromText")
    @ResponseWrapper(localName = "getHtmlSegmentationFromTextResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromTextResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromTextRequest", output = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromTextResponse")
    public String getHtmlSegmentationFromText(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "text", targetNamespace = "")
        String text);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getHtmlSegmentationFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromBinary")
    @ResponseWrapper(localName = "getHtmlSegmentationFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromBinaryResponse")
    public String getHtmlSegmentationFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getHtmlSegmentationFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromFile")
    @ResponseWrapper(localName = "getHtmlSegmentationFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetHtmlSegmentationFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromFileRequest", output = "http://server.thesemproject.org/SemService/getHtmlSegmentationFromFileResponse")
    public String getHtmlSegmentationFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @param text
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSegmentationFromText", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromText")
    @ResponseWrapper(localName = "getSegmentationFromTextResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromTextResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getSegmentationFromTextRequest", output = "http://server.thesemproject.org/SemService/getSegmentationFromTextResponse")
    public String getSegmentationFromText(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "text", targetNamespace = "")
        String text);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSegmentationFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromBinary")
    @ResponseWrapper(localName = "getSegmentationFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getSegmentationFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getSegmentationFromBinaryResponse")
    public String getSegmentationFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getSegmentationFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromFile")
    @ResponseWrapper(localName = "getSegmentationFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetSegmentationFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getSegmentationFromFileRequest", output = "http://server.thesemproject.org/SemService/getSegmentationFromFileResponse")
    public String getSegmentationFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTextFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetTextFromBinary")
    @ResponseWrapper(localName = "getTextFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetTextFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getTextFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getTextFromBinaryResponse")
    public String getTextFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFormattedTextFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetFormattedTextFromBinary")
    @ResponseWrapper(localName = "getFormattedTextFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetFormattedTextFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getFormattedTextFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getFormattedTextFromBinaryResponse")
    public String getFormattedTextFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFormattedTextFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetFormattedTextFromFile")
    @ResponseWrapper(localName = "getFormattedTextFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetFormattedTextFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getFormattedTextFromFileRequest", output = "http://server.thesemproject.org/SemService/getFormattedTextFromFileResponse")
    public String getFormattedTextFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @param text
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getClassificationsFromText", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromText")
    @ResponseWrapper(localName = "getClassificationsFromTextResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromTextResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getClassificationsFromTextRequest", output = "http://server.thesemproject.org/SemService/getClassificationsFromTextResponse")
    public String getClassificationsFromText(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "text", targetNamespace = "")
        String text);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getClassificationsFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromBinary")
    @ResponseWrapper(localName = "getClassificationsFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getClassificationsFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getClassificationsFromBinaryResponse")
    public String getClassificationsFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getClassificationsFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromFile")
    @ResponseWrapper(localName = "getClassificationsFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationsFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getClassificationsFromFileRequest", output = "http://server.thesemproject.org/SemService/getClassificationsFromFileResponse")
    public String getClassificationsFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns byte[]
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImageFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImageFromBinary")
    @ResponseWrapper(localName = "getImageFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImageFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getImageFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getImageFromBinaryResponse")
    public byte[] getImageFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param binary
     * @return
     *     returns java.util.List<byte[]>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImagesFromBinary", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImagesFromBinary")
    @ResponseWrapper(localName = "getImagesFromBinaryResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImagesFromBinaryResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getImagesFromBinaryRequest", output = "http://server.thesemproject.org/SemService/getImagesFromBinaryResponse")
    public List<byte[]> getImagesFromBinary(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "binary", targetNamespace = "")
        byte[] binary);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.util.List<byte[]>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImagesFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImagesFromFile")
    @ResponseWrapper(localName = "getImagesFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImagesFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getImagesFromFileRequest", output = "http://server.thesemproject.org/SemService/getImagesFromFileResponse")
    public List<byte[]> getImagesFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getClassificationTree", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationTree")
    @ResponseWrapper(localName = "getClassificationTreeResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetClassificationTreeResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getClassificationTreeRequest", output = "http://server.thesemproject.org/SemService/getClassificationTreeResponse")
    public String getClassificationTree(
        @WebParam(name = "server", targetNamespace = "")
        String server);

    /**
     * 
     * @param duration
     * @param server
     * @param startPeriod
     * @param score
     * @param fieldConditionValue
     * @param field
     * @param endPeriod
     * @param durationCondition
     * @param name
     * @param fieldConditionOperator
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addEvaluation", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.AddEvaluation")
    @ResponseWrapper(localName = "addEvaluationResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.AddEvaluationResponse")
    @Action(input = "http://server.thesemproject.org/SemService/addEvaluationRequest", output = "http://server.thesemproject.org/SemService/addEvaluationResponse")
    public String addEvaluation(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "name", targetNamespace = "")
        String name,
        @WebParam(name = "field", targetNamespace = "")
        String field,
        @WebParam(name = "fieldConditionOperator", targetNamespace = "")
        String fieldConditionOperator,
        @WebParam(name = "fieldConditionValue", targetNamespace = "")
        String fieldConditionValue,
        @WebParam(name = "startPeriod", targetNamespace = "")
        int startPeriod,
        @WebParam(name = "endPeriod", targetNamespace = "")
        int endPeriod,
        @WebParam(name = "duration", targetNamespace = "")
        double duration,
        @WebParam(name = "durationCondition", targetNamespace = "")
        String durationCondition,
        @WebParam(name = "score", targetNamespace = "")
        double score);

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns byte[]
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getImageFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImageFromFile")
    @ResponseWrapper(localName = "getImageFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetImageFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getImageFromFileRequest", output = "http://server.thesemproject.org/SemService/getImageFromFileResponse")
    public byte[] getImageFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServersNames", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetServersNames")
    @ResponseWrapper(localName = "getServersNamesResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetServersNamesResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getServersNamesRequest", output = "http://server.thesemproject.org/SemService/getServersNamesResponse")
    public String getServersNames();

    /**
     * 
     * @param server
     * @param fileName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTextFromFile", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetTextFromFile")
    @ResponseWrapper(localName = "getTextFromFileResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetTextFromFileResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getTextFromFileRequest", output = "http://server.thesemproject.org/SemService/getTextFromFileResponse")
    public String getTextFromFile(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName);

    /**
     * 
     * @param server
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServerDetails", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetServerDetails")
    @ResponseWrapper(localName = "getServerDetailsResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.GetServerDetailsResponse")
    @Action(input = "http://server.thesemproject.org/SemService/getServerDetailsRequest", output = "http://server.thesemproject.org/SemService/getServerDetailsResponse")
    public String getServerDetails(
        @WebParam(name = "server", targetNamespace = "")
        String server);

    /**
     * 
     * @param server
     * @param evaluatorName
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "clearEvaluation", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.ClearEvaluation")
    @ResponseWrapper(localName = "clearEvaluationResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.ClearEvaluationResponse")
    @Action(input = "http://server.thesemproject.org/SemService/clearEvaluationRequest", output = "http://server.thesemproject.org/SemService/clearEvaluationResponse")
    public String clearEvaluation(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "evaluatorName", targetNamespace = "")
        String evaluatorName);

    /**
     * 
     * @param server
     * @param max
     * @param text
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "tagCloud", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.TagCloud")
    @ResponseWrapper(localName = "tagCloudResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.TagCloudResponse")
    @Action(input = "http://server.thesemproject.org/SemService/tagCloudRequest", output = "http://server.thesemproject.org/SemService/tagCloudResponse")
    public String tagCloud(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "text", targetNamespace = "")
        String text,
        @WebParam(name = "max", targetNamespace = "")
        int max);

    /**
     * 
     * @param server
     * @param texts
     * @param max
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "tagClouds", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.TagClouds")
    @ResponseWrapper(localName = "tagCloudsResponse", targetNamespace = "http://server.thesemproject.org/", className = "org.thesemproject.server.TagCloudsResponse")
    @Action(input = "http://server.thesemproject.org/SemService/tagCloudsRequest", output = "http://server.thesemproject.org/SemService/tagCloudsResponse")
    public String tagClouds(
        @WebParam(name = "server", targetNamespace = "")
        String server,
        @WebParam(name = "texts", targetNamespace = "")
        List<String> texts,
        @WebParam(name = "max", targetNamespace = "")
        int max);

}
