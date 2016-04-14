

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class HackedInputStream extends ObjectInputStream{
		/**
		 * En input stream som konverterat om objekten till rätt klass.
		 * @param in Inputstream En vanlig input stream.
		 * @throws IOException
		 */
	    public HackedInputStream(InputStream in) throws IOException {
	        super(in);
	    }

	    @Override
	    /**
	     * Metod som skickar till rätt paket.
	     */
	    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
	        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();
/**
	        if (resultClassDescriptor.getName().equals("com.grabandgo.gng.gng.Day"))
	            resultClassDescriptor = ObjectStreamClass.lookup(SpargrisenObjekt.User.class);
	        if (resultClassDescriptor.getName().equals("com.example.girondins.pennies.pennies.objects.CategoryList"))
	            resultClassDescriptor = ObjectStreamClass.lookup(SpargrisenObjekt.CategoryList.class);
	        if (resultClassDescriptor.getName().equals("com.example.girondins.pennies.pennies.objects.Category"))
	            resultClassDescriptor = ObjectStreamClass.lookup(SpargrisenObjekt.Category.class);
	        if (resultClassDescriptor.getName().equals("com.example.girondins.pennies.pennies.objects.Purchase"))
	            resultClassDescriptor = ObjectStreamClass.lookup(SpargrisenObjekt.Purchase.class);
	    **/    
	        

	        return resultClassDescriptor;
	    }
	}

