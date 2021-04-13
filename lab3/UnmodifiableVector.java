public final class UnmodifiableVector{
    private final int size;
    private final double[] vector;

    public UnmodifiableVector(int m){
        size = m;
        vector = new double[m]; 
    }

    public UnmodifiableVector(double[] v){
        size = v.length;
        vector = v.clone();
    }

    public UnmodifiableVector(Vector v){
        size = v.getSize();
        vector = new double[size];
        for(int i=0;i<size;++i){
            vector[i] = v.getValue(i);
        }
    }

    public double[] add(Vector v){
        if(size!=v.getSize()){
            System.out.println("Wrong size vector in add!");
            return null;
        }
        double[] result = new double[size];
        for(int i=0;i<size;++i){
            result[i] = vector[i]+v.getValue(i);
        }
        return result;
    }

    public double dot(Vector v){
        if(size!=v.getSize()){
            System.out.println("Wrong size vector in dot!");
            return 0;
        }
        double result =0;
        for(int i=0;i<size;++i){
            result += vector[i]*v.getSize();
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
