

public final class UnmodifiableMatrix{
    private final int row, column;
    private final double[][] matrix;


    public UnmodifiableMatrix(int m, int n){
        this.row = m;
        this.column = n;
        matrix = new double[m][n];
    }

    public UnmodifiableMatrix(Matrix m){
        row = m.getRowNum();
        column = m.getColumnNum();
        matrix = new double[m.getRowNum()][];
        for(int i=0;i<m.getRowNum();++i){
            for(int j=0;j<m.getColumnNum();++j){
                matrix[i][j]=m.getValue(i, j);
            }
        }
    }

    public UnmodifiableMatrix(double[][] arr){
        row = arr.length;
        column = arr[0].length;
        matrix = new double[row][];
        for(int i=0;i<arr.length;++i){
            matrix[i] = arr[i].clone();
        }
    }

    public void setValue(double value, int m, int n){
        matrix[m][n] = value;
    }



    public void display(){
        for(int i=0;i<row;++i){
            System.out.println(matrix[i].toString());
        }
    }

    public double[][] add(Matrix m){
        if(m.getRowNum() != row || m.getColumnNum() != column){
            System.out.println("Wrong size matrix in add!");
            return null;
        }
        double[][] result = new double[row][column];
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                result[i][j]=m.getValue(i, j) + matrix[i][j];
            }
        }
        return result;
    }

    public double[][] multi(Matrix m){
        if(column!=m.getRowNum()){
            System.out.println("Wrong size matrix in mutiple!");
            return null;
        }
        double[][] result = new double[row][m.getColumnNum()];
        for(int i=0;i<row;++i){
            for(int j=0;j<m.getColumnNum();++j){
                int sum =0;
                for(int k=0;k<getColumnNum();++k){
                    sum += matrix[i][k]*m.getValue(k, j);
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public double[] multi(Vector v){
        if(column!=v.getSize()){
            return null;
        }
        double[] result = new double[row];
        for(int i=0;i<row;++i){
            int sum = 0;
            for(int k=0;k<column;++i){
                sum+=matrix[i][k]*v.getValue(k);
            }
            result[i] = sum;
        }
        return result;
    }

   public double getValue(int m, int n){
       return matrix[m][n];
   }

   public int getRowNum(){
       return row;
   }

   public int getColumnNum(){
       return column;
   }

}
