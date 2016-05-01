/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.descriptors;

import net.sf.jasperreports.charts.type.EdgeEnum;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPLegendAlignementEnum;

public class EdgePropertyDescriptor extends NamedEnumPropertyDescriptor<EdgeEnum> {

	public EdgePropertyDescriptor(Object id, String displayName, NullEnum type) {
		super(id, displayName, EdgeEnum.BOTTOM, type);
	}

	public ASPropertyWidget<NamedEnumPropertyDescriptor<EdgeEnum>> createWidget(Composite parent, AbstractSection section) {
		ASPropertyWidget<NamedEnumPropertyDescriptor<EdgeEnum>> widget = new SPLegendAlignementEnum<NamedEnumPropertyDescriptor<EdgeEnum>>(
				parent, section, this);
		HelpSystem.bindToHelp(this, widget.getControl());
		return widget;
	}

}
