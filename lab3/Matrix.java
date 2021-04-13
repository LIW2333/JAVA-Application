
import java.util.*;
import java.text.*;
public class Matrix{
    private int row, column;
    private double[][] matrix;


    public Matrix(int m, int n){
        this.row = m;
        this.column = n;
        matrix = new double[m][n];
    }

    public Matrix(Matrix m){
        row = m.row;
        column = m.column;
        matrix = new double[m.row][];
        for(int i=0;i<m.matrix.length;++i){
            matrix[i] = m.matrix[i].clone();
        }
    }

    public Matrix(double[][] arr){
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
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public double[][] add(Matrix m){
        if(m.row != row || m.column != column){
            System.out.println("Wrong size matrix in add!");
            return null;
        }
        double[][] result = new double[row][column];
        for(int i=0;i<row;++i){
            for(int j=0;j<column;++j){
                result[i][j]=m.matrix[i][j] + matrix[i][j];
            }
        }
        return result;
    }

    public double[][] multi(Matrix m){
        if(column!=m.row){
            System.out.println("Wrong size matrix in mutiple!");
            return null;
        }
        double[][] result = new double[row][m.column];
        for(int i=0;i<row;++i){
            for(int j=0;j<m.column;++j){
                int sum =0;
                for(int k=0;k<column;++k){
                    sum += matrix[i][k]*m.matrix[k][j];
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
