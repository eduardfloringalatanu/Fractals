public class ComplexNumber {
	float a, b;
	
	ComplexNumber() {
		this.a = 0.0f;
		this.b = 0.0f;
	}
	
	ComplexNumber(float a, float b) {
		this.a = a;
		this.b = b;
	}
	
	public ComplexNumber add(ComplexNumber z) {
		return new ComplexNumber(this.a + z.a, this.b + z.b);
	}
	
	public ComplexNumber subtract(ComplexNumber z) {
		return new ComplexNumber(this.a - z.a, this.b - z.b);
	}
	
	public ComplexNumber multiply(ComplexNumber z) {
		return new ComplexNumber(this.a * z.a - this.b * z.b, this.a * z.b + this.b * z.a);
	}
	
	public ComplexNumber power(int n) {
		ComplexNumber z = this;
		
		for (int i = 1; i < n; ++i)
			z = z.multiply(this);
		
		return z;
	}
	
	public float absolute() {
		return (float)Math.sqrt(this.a * this.a + this.b * this.b);
	}
	
	public ComplexNumber conjugate() {
		return new ComplexNumber(this.a, -this.b);
	}
	
	public ComplexNumber divide(ComplexNumber z) {
		ComplexNumber numerator = this.multiply(z.conjugate());
		float denominator = z.a * z.a + z.b * z.b;
		
		return new ComplexNumber(numerator.a / denominator, numerator.b / denominator);
	}
}
