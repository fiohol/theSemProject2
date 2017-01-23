/*
 * Copyright 2017 The Sem Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thesemproject.commons.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.codehaus.plexus.util.FileUtils;
import org.thesemproject.commons.classification.IClassificationPath;

/**
 *
 * @author The Sem Project
 */
public class CommonUtils {
    /**
     * Scrive un CSV in UTF-8
     *
     * @param fileName nome del file
     * @param lines righe
     * @return File salvato
     */
    public static File writeCSV(String fileName, List<String> lines) {
        File f = new File(fileName);
        try {
            if (f.exists()) {
                f.delete();
            }
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
            for (String word : lines) {
                // String value = new String(word.getBytes(), "UTF-8");
                out.write(word + "\r\n");
            }
            out.close();
        } catch (Exception e) {
            LogGui.printException(e);
        }
        return f;
    }
    
    /**
     * legge un xml
     *
     * @param file file
     * @return JDOM
     * @throws Exception Eccezione
     */
    public static Document readXml(String file) throws Exception {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(file);
    }

    /**
     * Salva un JDOM su file
     *
     * @param doc JDOM
     * @param file percorso di salvataggio
     */
    public static void storeXml(Document doc, String file) {
        if (doc != null) {
            makeBackup(file);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            try {
                FileOutputStream fos = new FileOutputStream(file);
                outputter.output(doc, fos);
                fos.close();
            } catch (IOException e) {
                LogGui.printException(e);
            }
        }
    }
    
    /**
     * gestice i backup di un file
     *
     * @param fileName nome del file che si vuole backuppare
     */
    public static void makeBackup(String fileName) {
        try {
            File original = new File(fileName);
            if (original.exists()) {
                String localPath = original.getParent() + "/backup";
                File lp = new File(localPath);
                if (!lp.exists()) {
                    lp.mkdirs();
                }
                String savingFileName = localPath + "/" + original.getName();
                String pattern = savingFileName;
                long oldestTime = System.currentTimeMillis();
                for (int i = 0; i < 20; i++) {
                    String fn = pattern + "." + i + ".bck";
                    File back = new File(fn);
                    if (!back.exists()) {
                        savingFileName = fn;
                        break;
                    } else if (back.lastModified() < oldestTime) {
                        oldestTime = back.lastModified();
                        savingFileName = back.getAbsolutePath();
                    }
                }
                FileUtils.copyFile(original, new File(savingFileName));
            }
        } catch (Exception e) {
            LogGui.printException(e);
        }
    }
    
    
     /**
     * Converte un path di classificazione da stringa a classification path
     *
     * @param path path stringa
     * @return path di classificazione
     */
    public static IClassificationPath getClassificationPath(String path, IClassificationPath cp) {
        String[] categories = path.split(">");
        
        for (int i = 0; i < categories.length; i++) {
            cp.addResult(StringUtils.firstUpper(categories[i]), 1, i);
        }
        return cp;
    }
}
