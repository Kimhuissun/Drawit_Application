package com.makeit.sunnycar.drawit.data;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gmltj on 2018-05-25.
 */

public class Pen {
    public List<Path> pathArrayList= Collections.synchronizedList(new ArrayList<Path>());
    public List<Paint> paintArrayList=Collections.synchronizedList(new ArrayList<Paint>());

    public synchronized void redo(Path path, Paint paint, Canvas pathCanvas){
        pathArrayList.add(path);
        paintArrayList.add(paint);

        redraw(pathCanvas,false);

    }

    public synchronized void undo(Canvas canvas) {

            redraw(canvas,false);

    }
    public synchronized void addSketch(Canvas canvas) {
        Iterator<Path> pathIterator=pathArrayList.iterator();
        Iterator<Paint> paintIterator=paintArrayList.iterator();

        while (pathIterator.hasNext()&&paintIterator.hasNext()){
            canvas.drawPath(pathIterator.next(), paintIterator.next());

        }
    }
    public synchronized void redraw(Canvas canvas,boolean isSaveMode){
        Iterator<Path> pathIterator=pathArrayList.iterator();
        Iterator<Paint> paintIterator=paintArrayList.iterator();
         while (pathIterator.hasNext()&&paintIterator.hasNext()){
                canvas.drawPath(pathIterator.next(), paintIterator.next());

            }

    }

    public synchronized void addPen() {
        pathArrayList.add(new Path());
        paintArrayList.add(new Paint());


    }


    public synchronized void removeCurrentPen() {
        if(pathArrayList.size()>0&&paintArrayList.size()>0){
            pathArrayList.remove(pathArrayList.size()-1);
            paintArrayList.remove(paintArrayList.size()-1);
        }

    }

    public synchronized Path getCurrentPath() {
        if(pathArrayList.size()>0)
            return pathArrayList.get(pathArrayList.size()-1);
        else return null;
    }

    public synchronized Paint getCurrentPaint() {
        if(paintArrayList.size()>0)
            return paintArrayList.get(paintArrayList.size()-1);
        else return null;
    }

    public synchronized Path removeCurrentPath() {
        if(pathArrayList.size()>0)
            return pathArrayList.remove(paintArrayList.size()-1);
        else return null;
    }

    public synchronized Paint removeCurrentPaint() {
        if(paintArrayList.size()>0)
            return paintArrayList.remove(paintArrayList.size()-1);
        else return null;
    }

    public synchronized void removeAllPen() {
        paintArrayList.clear();
        pathArrayList.clear();
    }


}
