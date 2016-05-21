package am2.api.math;

public class AMPlane{
	AMVector3 planeDef[];

	public AMPlane(AMVector3... plane){
		planeDef = plane;
	}

	private AMVector3 planeNormal(){
		AMVector3 vVector1 = new AMVector3(planeDef[2], planeDef[0]);
		AMVector3 vVector2 = new AMVector3(planeDef[1], planeDef[0]);

		AMVector3 vNormal = AMVector3.crossProduct(vVector1, vVector2);

		vNormal.normalize();
		return vNormal;
	}

	private float planeDistance(AMVector3 normal, AMVector3 point){
		float distance = 0;
		distance = -((normal.x * point.x) + (normal.y * point.y) + (normal.z * point.z));

		return distance;
	}

	public boolean lineSegmentCrosses(AMVector3 p1, AMVector3 p2){
		float distance1 = 0, distance2 = 0;

		AMVector3 vNormal = planeNormal();

		float originDistance = planeDistance(vNormal, planeDef[0]);

		distance1 = ((vNormal.x * p1.x) +                    // Ax +
				(vNormal.y * p1.y) +                    // Bx +
				(vNormal.z * p1.z)) + originDistance;    // Cz + D

		distance2 = ((vNormal.x * p2.x) +                    // Ax +
				(vNormal.y * p2.y) +                    // Bx +
				(vNormal.z * p2.z)) + originDistance;    // Cz + D

		return distance1 * distance2 < 0;

	}
}
