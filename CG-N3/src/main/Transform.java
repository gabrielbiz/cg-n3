package main;

public class Transform {

	static final double DEG_TO_RAD = 0.017453292519943295769236907684886;

	private double[] matriz = {	
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1};

	/**
	 * Atribuí uma matriz identidade para esta transformação
	 */
	public void setIdentity() {
		for (int i=0; i<16; ++i) {
			matriz[i] = 0.0;
		}
		matriz[0] = matriz[5] = matriz[10] = matriz[15] = 1.0;
	}

	/**
	 * Realiza uma translação
	 * 
	 * @param tx translação em X
	 * @param ty translação em Y
	 * @param tz translação em Z
	 */
	public void translate(double tx, double ty, double tz)
	{
		setIdentity();
	    matriz[12] = tx;
	    matriz[13] = ty;
	    matriz[14] = tz;
	}

	/**
	 * Atribuí uma escala, sendo 1 a escala original
	 * 
	 * @param sX escala em X
	 * @param sY escala em Y
	 * @param sZ escala em Z
	 */
	public void scale(double sX, double sY, double sZ)
	{
	    setIdentity();
	    matriz[0] =  sX;
	    matriz[5] =  sY;
	    matriz[10] = sZ;
	}
	
	/**
	 * Rotaciona o eixo X
	 * 
	 * @param radians graus, onde zero é netro
	 */
	public void rotateX(double radians)
	{
	    setIdentity();
	    matriz[5] =   Math.cos(radians);
	    matriz[9] =  -Math.sin(radians);
	    matriz[6] =   Math.sin(radians);
	    matriz[10] =  Math.cos(radians);
	}

	/**
	 * Rotaciona o eixo Y
	 * 
	 * @param radians graus, onde zero é netro
	 */
	public void rotateY(double radians)
	{
		setIdentity();
	    matriz[0] =   Math.cos(radians);
	    matriz[8] =   Math.sin(radians);
	    matriz[2] =  -Math.sin(radians);
	    matriz[10] =  Math.cos(radians);
	}

	/**
	 * Rotaciona o eixo Z
	 * 
	 * @param radians graus, onde zero é netro
	 */
	public void rotateZ(double radians)
	{
	    setIdentity();
	    matriz[0] =  Math.cos(radians);
	    matriz[4] = -Math.sin(radians);
	    matriz[1] =  Math.sin(radians);
	    matriz[5] =  Math.cos(radians);
	}

	public Transform getInverseMatriz() {
		Transform result = new Transform();
		result.setData(matriz);
		result.matriz[12] *= -1;
	    result.matriz[13] *= -1;
	    result.matriz[14] *= -1;
	    result.matriz[0] = 1/matriz[0];
	    result.matriz[5] = 1/matriz[5];
	    result.matriz[10] = 1/matriz[10];
		return result;
	}
	
	public Point4D transformPoint(Point4D point) {
		Point4D pointResult = new Point4D(
				(int) (matriz[0]*point.getX()  + matriz[4]*point.getY() + matriz[8]*point.getZ()  + matriz[12]*point.getW()),
				(int) (matriz[1]*point.getX()  + matriz[5]*point.getY() + matriz[9]*point.getZ()  + matriz[13]*point.getW()),
				(int) (matriz[2]*point.getX()  + matriz[6]*point.getY() + matriz[10]*point.getZ() + matriz[14]*point.getW()),
				(int) (matriz[3]*point.getX()  + matriz[7]*point.getY() + matriz[11]*point.getZ() + matriz[15]*point.getW()));
		return pointResult;
	}

	public Transform transformMatrix(Transform t) {
		Transform result = new Transform();
	    for (int i=0; i < 16; ++i)
        result.matriz[i] =
              matriz[i%4]    *t.matriz[i/4*4]  +matriz[(i%4)+4] *t.matriz[i/4*4+1]
            + matriz[(i%4)+8]*t.matriz[i/4*4+2]+matriz[(i%4)+12]*t.matriz[i/4*4+3];
	    return result;
	}
	
	public double getElement(int index) {
		return matriz[index];
	}
	
	public void setElement(int index, double value) {
		matriz[index] = value;
	}

	public double[] getDate() {
		return matriz;	
	}
	
	public void setData(double[] data)
	{
	    int i;

	    for (i=0; i<16; i++)
	    {
	        matriz[i] = (data[i]);
	    }
	}

	public void exibeMatriz() {
		System.out.println("______________________");
		System.out.println("|" + getElement( 0) + " | "+ getElement( 4) + " | " + getElement( 8) + " | "+ getElement(12));
		System.out.println("|" + getElement( 1) + " | "+ getElement( 5) + " | " + getElement( 9) + " | "+ getElement(13));
		System.out.println("|" + getElement( 2) + " | "+ getElement( 6) + " | " + getElement(10) + " | "+ getElement(14));
		System.out.println("|" + getElement( 3) + " | "+ getElement( 7) + " | " + getElement(11) + " | "+ getElement(15));
	}

	
}
