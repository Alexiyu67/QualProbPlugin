package fr.systerel.rodinextension.sample.ui.navigator;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eventb.core.IMachineRoot;
import org.rodinp.core.RodinDBException;

import fr.systerel.rodinextension.sample.basis.IBound;

public class BoundContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// ignore
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// ignore
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parent) {
		Object[] objects = new Object[0];
		if (parent instanceof IMachineRoot) {
			try {
				objects = ((IMachineRoot)parent).getChildrenOfType(
						IBound.ELEMENT_TYPE);
			} catch (RodinDBException e) {
				e.printStackTrace();
			}
		}
		return objects;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IBound) {
			((IBound) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}