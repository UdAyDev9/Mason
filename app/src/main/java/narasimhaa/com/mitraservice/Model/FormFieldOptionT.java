package narasimhaa.com.mitraservice.Model;

public class FormFieldOptionT implements Cloneable {

	private Integer _id, componentType,validRule;
	private String optionId, optionValue, dependentFieldId, formId, fieldId,
			fieldName,validation,label;


	public Integer getValidRule() {
		return validRule;
	}

	public void setValidRule(Integer validRule) {
		this.validRule = validRule;
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public Integer getComponentType() {
		return componentType;
	}

	public void setComponentType(Integer componentType) {
		this.componentType = componentType;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getDependentFieldId() {
		return dependentFieldId;
	}

	public void setDependentFieldId(String dependentFieldId) {
		this.dependentFieldId = dependentFieldId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	

}
