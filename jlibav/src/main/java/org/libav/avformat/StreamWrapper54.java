/*
 * Copyright (C) 2012 Ondrej Perutka
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library. If not, see 
 * <http://www.gnu.org/licenses/>.
 */
package org.libav.avformat;

import org.bridj.Pointer;
import org.libav.avcodec.CodecContextWrapperFactory;
import org.libav.avcodec.ICodecContextWrapper;
import org.libav.avformat.bridge.AVStream54;
import org.libav.avutil.DictionaryWrapperFactory;
import org.libav.avutil.IDictionaryWrapper;
import org.libav.avutil.bridge.AVUtilLibrary;
import org.libav.bridge.LibraryManager;
import org.libav.util.Rational;

/**
 * Wrapper class for the AVStream54.
 * 
 * @author Ondrej Perutka
 */
public class StreamWrapper54 extends AbstractStreamWrapper {
    
    private AVStream54 stream;
    
    /**
     * Create a new wrapper for the given AVStream.
     * 
     * @param stream an AVStream structure
     */
    public StreamWrapper54(AVStream54 stream) {
        this.stream = stream;
    }

    @Override
    public void clearWrapperCache() {
        super.clearWrapperCache();
        
        rebindCodecContext();
        rebindMetadata();
    }
    
    private void rebindCodecContext() {
        if (stream == null || codecContext == null)
            return;
        
        Pointer<?> ptr = stream.codec();
        if (ptr == null)
            codecContext = null;
        else
            codecContext.rebind(ptr);
    }
    
    private void rebindMetadata() {
        if (stream == null || metadata == null)
            return;
        
        Pointer<?> ptr = stream.metadata();
        if (ptr == null)
            metadata = null;
        else
            metadata.rebind(ptr);
    }
    
    @Override
    public Pointer<?> getPointer() {
        if (stream == null)
            return null;
        
        return Pointer.pointerTo(stream);
    }

    @Override
    public void rebind(Pointer<?> pointer) {
        stream = new AVStream54(pointer);
    }
    
    @Override
    public void free() {
        if (stream == null)
            return;
        
        AVUtilLibrary lib = LibraryManager.getInstance().getAVUtilLibrary();
        lib.av_free(getPointer());
        stream = null;
    }
    
    @Override
    public ICodecContextWrapper getCodecContext() {
        if (stream == null)
            return null;
        
        if (codecContext == null) {
            Pointer<?> p = stream.codec();
            codecContext = p == null ? null : CodecContextWrapperFactory.getInstance().wrap(p);
        }
        
        return codecContext;
    }
    
    @Override
    public void setCodecContext(ICodecContextWrapper codecContext) {
        if (stream == null)
            return;
        
        stream.codec(codecContext == null ? null : codecContext.getPointer());
        this.codecContext = codecContext;
    }
    
    @Override
    public int getIndex() {
        if (stream == null)
            return -1;
        
        if (index == null)
            index = stream.index();
        
        return index;
    }

    @Override
    public Rational getSampleAspectRatio() {
        if (stream == null)
            return null;
        
        if (sampleAspectRatio == null)
            sampleAspectRatio = new Rational(stream.sample_aspect_ratio());
        
        return sampleAspectRatio;
    }

    @Override
    public void setSampleAspectRatio(Rational sampleAspectRatio) {
        if (stream == null)
            return;
        
        if (sampleAspectRatio == null)
            sampleAspectRatio = new Rational(0, 0);
        
        stream.sample_aspect_ratio().num((int)sampleAspectRatio.getNumerator());
        stream.sample_aspect_ratio().den((int)sampleAspectRatio.getDenominator());
        this.sampleAspectRatio = sampleAspectRatio;
    }

    @Override
    public Rational getTimeBase() {
        if (stream == null)
            return null;
        
        if (timeBase == null)
            timeBase = new Rational(stream.time_base());
        
        return timeBase;
    }
    
    @Override
    public void setTimeBase(Rational timeBase) {
        if (stream == null)
            return;
        
        if (timeBase == null)
            timeBase = new Rational(0, 0);
        
        stream.time_base().num((int)timeBase.getNumerator());
        stream.time_base().den((int)timeBase.getDenominator());
        this.timeBase = timeBase;
    }

    @Override
    public long getFrameCount() {
        if (stream == null)
            return -1;
        
        if (frameCount == null)
            frameCount = stream.nb_frames();
        
        return frameCount;
    }
    
    @Override
    public long getDuration() {
        if (stream == null)
            return -1;
        
        if (duration == null)
            duration = stream.duration();
        
        return duration;
    }

    @Override
    public IDictionaryWrapper getMetadata() {
        if (stream == null)
            return null;
        
        if (metadata == null) {
            DictionaryWrapperFactory dwf = DictionaryWrapperFactory.getInstance();
            if (stream.metadata() == null) {
                metadata = dwf.allocate();
                stream.metadata(metadata.getPointer());
            } else
                metadata = dwf.wrap(stream.metadata());
        }
        
        return metadata;
    }

    @Override
    public int getDisposition() {
        if (stream == null)
            return 0;
        
        if (disposition == null)
            disposition = stream.disposition();
        
        return disposition;
    }
    
}
