package com.arcgis.mymap.Export;

import android.os.Environment;

import com.arcgis.mymap.contacts.MoreLines;
import com.arcgis.mymap.contacts.SurFace;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class GeoWriteSurfaceGpx {
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;
    public void createsurfaceGpx (String dirName, List<SurFace> list, String time)throws Exception{
        Element root= DocumentHelper.createElement("gpx");
        Document document=DocumentHelper.createDocument(root);
        //给根节点gpx添加属性
        root.addAttribute(" version","1.1")
                .addAttribute("creator","nl.sogeti.android.gpstracker" )
                .addAttribute("xsi:schemaLocation","http://www.topografix.com/GPX/1/1 http://www.topografix.com/gpx/1/1/gpx.xsd")
                .addAttribute("xmlns","http://www.topografix.com/GPX/1/1")
                .addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance")
                .addAttribute("xmlns:gpx10","http://www.topografix.com/GPX/1/0")
                .addAttribute("xmlns:ogt10","http://gpstracker.android.sogeti.nl/GPX/1/0");
        //给根节点kml添加子节点  metadata
        Element metadatadocument=root.addElement("metadata");
        Element filename=metadatadocument.addElement("name").addText(dirName+".gpx");
        Element filetime=metadatadocument.addElement("time").addText(time);
        //给根节点kml添加子节点  trk
        for (int i=0;i<list.size();i++){
            Element trkdocument=root.addElement("trk");
            Element descdocument=trkdocument.addElement("desc").addText(list.get(i).getClassification());
            Element trksegdocument=trkdocument.addElement("trkseg");
            for (int k=0;k<list.get(i).getListla().size();i++){
                Element trkptdocumentk=trksegdocument.addElement("trkpt");
                trkptdocumentk.addAttribute("lat",list.get(i).getListln().get(k))
                        .addAttribute("lon",list.get(i).getListla().get(k));
                Element eledocumentk=trkptdocumentk.addElement("ele").addText("0");
                Element timedocumentk=trkptdocumentk.addElement("time").addText(time);
            }
            Element trkptdocument=trksegdocument.addElement("trkpt");
            trkptdocument.addAttribute("lat",list.get(i).getListln().get(0))
                    .addAttribute("lon",list.get(i).getListla().get(0));
            Element eledocumentk=trkptdocument.addElement("ele").addText("0");
            Element timedocumentk=trkptdocument.addElement("time").addText(time);
        }
        String state= Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            mExternalStorageAvailable=mExternalStorageWriteable=true;
        }else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            mExternalStorageAvailable=true;
            mExternalStorageWriteable=false;
        }else {
            mExternalStorageWriteable=mExternalStorageAvailable=false;
        }
        //将生成的gpx写出本地
        OutputFormat format=OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");//设置编码格式
        format.setNewlines(true);//设置换行
        //将doc.kml写入到/mnt/sdcard/Mymap/doc.kml目录
        File file=new File(Environment.getExternalStorageDirectory()+"/MyMap/地勘/Export");
        if (!file.exists()) {
            file.mkdirs();
        }
        String path=Environment.getExternalStorageDirectory()+"/Mymap/地勘/Export/"+dirName+".gpx";
        File file1=new File(path);
        if (file1.exists()){
            file1.delete();
        }
        file1.createNewFile();
        FileOutputStream outputStream=new FileOutputStream(path);
        XMLWriter xmlWriter = new XMLWriter(outputStream,format);
        xmlWriter.write(document);
        xmlWriter.close();
    }
}
