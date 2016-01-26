package com.iceddev.eyesocket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.iceddev.face.MainActivity;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.libcore.RequestHeaders;
import com.koushikdutta.async.http.server.AsyncHttpServer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.koushikdutta.async.http.server.AsyncHttpServer.WebSocketRequestCallback;

/**
 * Created by phated on 8/6/13.
 */
public class Server {

    List<WebSocket> sockets;

    AsyncHttpServer server;

    private Object lastData;

    public Server(int port, final MainActivity controller){
        sockets = new ArrayList<WebSocket>();

        server = new AsyncHttpServer();
        server.websocket("/", new WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, RequestHeaders requestHeaders) {
                Log.e("SERVER", "Connected " + webSocket.toString());
                // On initial connect, send the last bit of data
                if (lastData != null) {
                    webSocket.send(lastData.toString());
                }

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String imageString) {
                        String imageDataBytes = imageString.substring(imageString.indexOf(",") + 1);
                        Log.i("Image", imageDataBytes);
                        byte[] decodedString = Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        controller.updateViewerPNG(decodedByte);
                    }
                });

                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception e) {
                        Log.e("SERVER", "Disconnected " + webSocket.toString());
                        sockets.remove(webSocket);
                    }
                });

                sockets.add(webSocket);
            }
        });
        server.listen(port);
    }

    public void sendData(Object data) {
        lastData = data;
        for (WebSocket socket : sockets) {
            socket.send(data.toString());
        }
    }

    public void close() {
        for (WebSocket socket : sockets){
            socket.close();
        }
        server.stop();
    }

}
