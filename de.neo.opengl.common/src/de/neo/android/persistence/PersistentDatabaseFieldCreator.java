package de.neo.android.persistence;

import java.lang.reflect.Field;
import java.util.Date;

import de.neo.android.persistence.fields.PersistentBoolean;
import de.neo.android.persistence.fields.PersistentDate;
import de.neo.android.persistence.fields.PersistentDomainBase;
import de.neo.android.persistence.fields.PersistentEnum;
import de.neo.android.persistence.fields.PersistentField;
import de.neo.android.persistence.fields.PersistentInteger;
import de.neo.android.persistence.fields.PersistentText;

public class PersistentDatabaseFieldCreator implements PersistentFieldCreator {

	@Override
	public PersistentField createPersistentField(Field field, int number) {
		if (field.getType().equals(int.class))
			return new PersistentInteger(field, number);
		if (field.getType().equals(boolean.class))
			return new PersistentBoolean(field, number);
		if (field.getType().isEnum())
			return new PersistentEnum(field, number, field.getType());
		if (field.getType().equals(Date.class))
			return new PersistentDate(field, number);
		if (field.getType().equals(String.class))
			return new PersistentText(field, number);
		if (DomainBase.class.isAssignableFrom(field.getType()))
			return new PersistentDomainBase(field, number);
		return null;
	}

}
