package com.pacifyr.client.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by ceino on 17/5/18.
 */

public class ModelPacifyrDocuments  implements Parcelable{

    private String fileName;
    private String  filePath;
    private String fileType;
    private boolean isCameraImage;
    private boolean isPdfFile;
    private Bitmap bitmap;
    private Uri uri;
    private File file;

    public ModelPacifyrDocuments(){
    }

    public ModelPacifyrDocuments(Uri uri){
        this.uri=uri;
    }


    protected ModelPacifyrDocuments(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        fileType = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<ModelPacifyrDocuments> CREATOR = new Creator<ModelPacifyrDocuments>() {
        @Override
        public ModelPacifyrDocuments createFromParcel(Parcel in) {
            return new ModelPacifyrDocuments(in);
        }

        @Override
        public ModelPacifyrDocuments[] newArray(int size) {
            return new ModelPacifyrDocuments[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }



    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(fileType);
        dest.writeParcelable(bitmap, flags);
        dest.writeParcelable(uri, flags);
    }

    public boolean isCameraImage() {
        return isCameraImage;
    }

    public void setCameraImage(boolean cameraImage) {
        isCameraImage = cameraImage;
    }

    public boolean isPdfFile() {
        return isPdfFile;
    }

    public void setPdfFile(boolean pdfFile) {
        isPdfFile = pdfFile;
    }
}
