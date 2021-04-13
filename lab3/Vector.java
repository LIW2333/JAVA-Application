

public class Vector{
    private int size;
    private double[] vector;
     
    public Vector(int m){
        size = m;
        vector = new double[m]; 
    }

    public Vector(double[] v){
        size = v.length;
        vector = v.clone();
    }

    public Vector(Vector v){
        size = v.size;
        vector = v.vector.clone();
    }

    public void setValue(int index, double value){
        vector[index]=value;
    }

    public double[] add(Vector v){
        if(size!=v.size){
            System.out.println("Wrong size vector in add!");
            return null;
        }
        double[] result = new double[size];
        for(int i=0;i<size;++i){
            result[i] = vector[i]+v.vector[i];
        }
        return result;
    }

    public double dot(Vector v){
        if(size!=v.size){
            System.out.println("Wrong size vector in dot!");
            return 0;
        }
        double result =0;
        for(int i=0;i<size;++i){
            result += vector[i]*v.vector[i];
        }
        return result;
    }

    public double getValue(int index){
        return vector[index];
    }

    public void display(){
        System.out.println(vector.toString());
    }

    public int getSize(){
        return size;
    }

}
