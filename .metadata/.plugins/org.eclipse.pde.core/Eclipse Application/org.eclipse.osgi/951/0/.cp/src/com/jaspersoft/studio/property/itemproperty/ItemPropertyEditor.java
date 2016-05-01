/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.itemproperty;
 

import net.sf.jasperreports.components.items.StandardItemProperty;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.messages.Messages;

public class ItemPropertyEditor extends Wizard {
	private StandardItemProperty mExpression;
	private ExpressionContext exprContext;
	private ItemPropertyPage page0;

	public StandardItemProperty getValue() {
		if (page0 != null)
			return page0.getValue();
		return mExpression;
	}

	public void setValue(StandardItemProperty value) {
		if (page0 != null)
			page0.setValue(value);
		this.mExpression = value;
	}
	
	public void setExpressionContext(ExpressionContext exprContext){
		this.exprContext=exprContext;
		if(page0!=null){
			page0.setExpressionContext(this.exprContext);
		}
	}

	public ItemPropertyEditor() {
		super();
		setWindowTitle(Messages.common_expression_editor);
	}

	@Override
	public void addPages() {
		page0 = new ItemPropertyPage("jrquery.editor"); //$NON-NLS-1$
		page0.setValue(mExpression);
		if(exprContext!=null){
			page0.setExpressionContext(exprContext);
		}
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		// Remove any expression context reference
		ExpressionEditorSupportUtil.setCurrentExpressionContext(null);
		// Notify closing
		ExpressionEditorSupportUtil.notifyExpressionEditorDialogClosing();
	}
	
	
}
