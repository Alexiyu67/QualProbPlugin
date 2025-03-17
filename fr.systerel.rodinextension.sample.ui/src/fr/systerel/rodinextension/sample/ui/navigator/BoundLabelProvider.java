package fr.systerel.rodinextension.sample.ui.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eventb.internal.ui.eventbeditor.imageprovider.IImageProvider;
import org.eventb.ui.EventBUIPlugin;
import org.eventb.ui.IEventBSharedImages;
import org.rodinp.core.IRodinElement;

import fr.systerel.rodinextension.sample.basis.IBound;

public class BoundLabelProvider implements ILabelProvider, IImageProvider {

	private static final String BOUND_LABEL = "Bound";

	@Override
	public Image getImage(Object element) {
               if (element instanceof IBound) {
			return getImage();
		}
               return null;
	}

	public static Image getImage() {
		final ImageRegistry reg = EventBUIPlugin.getDefault()
				.getImageRegistry();
		return reg.get(IEventBSharedImages.IMG_CONSTANT);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IBound) {
			return BOUND_LABEL;
		}
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor(IRodinElement element) {
		final Image image = getImage(element);
		if (image == null) {
			return null;
		}
		final ImageDescriptor desc = ImageDescriptor.createFromImage(image);
		return desc;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}
 
   // other methods can be ignored...

}