package com.grabandgo.gng.gng;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class HackedInputStream extends ObjectInputStream{

	    public HackedInputStream(InputStream in) throws IOException {
	        super(in);
	    }

	    @Override
	    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
	        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

	        if (resultClassDescriptor.getName().equals("Day"))
	            resultClassDescriptor = ObjectStreamClass.lookup(Day.class);
	        if (resultClassDescriptor.getName().equals("Offers"))
	            resultClassDescriptor = ObjectStreamClass.lookup(Offers.class);
	        if (resultClassDescriptor.getName().equals("OpenHours"))
	            resultClassDescriptor = ObjectStreamClass.lookup(OpenHours.class);
            if (resultClassDescriptor.getName().equals("Restaurant"))
                resultClassDescriptor = ObjectStreamClass.lookup(Restaurant.class);
	        

	        return resultClassDescriptor;
	    }
	}

