import java.util.*;
import java.text.*;

public class Main{
 
    public static void main(String[] args) {
 
        Scanner scan = new Scanner(System.in);
        int row = scan.nextInt();
        int col = scan.nextInt();
        System.out.println("row:" + row +" column:" + col);
 
//         //set value
        
        double data[][] = new double[row][col];
 
        for(int i = 0;i<row;i++) {
            for(int j = 0;j<col;j++) {
                double d = scan.nextDouble();
                data[i][j] = d; 
            } 
        }
        System.out.println("---------Matrix: display---------");
        Matrix matrix = new Matrix(data);
        matrix.display();
        System.out.println("---------Matrix: set value---------"); 
        System.out.println("Input the row, column, and new value to reset the matrix value:");
        int srow = scan.nextInt(); 
        int scol = scan.nextInt(); 
        double sv = scan.nextDouble();
        System.out.println("after set value:");
        matrix.setValue(sv, srow, scol);
        for(int i=0;i<row;++i){
            for(int j=0;j<col;++j){
                System.out.print(matrix.getValue(i, j)+ " ");
            }
            System.out.println("");
        }
 
       //get value
 
        DecimalFormat df = new DecimalFormat("0");
        int vrow = scan.nextInt();
        int vcol = scan.nextInt();
        System.out.print("value on (" +vrow + "," + vcol +"):");
        System.out.println(df.format(matrix.getValue(vrow, vcol)));
 
        //add
        System.out.println("---------Matrix: add---------");
        int addrow = scan.nextInt();
        int addcol = scan.nextInt(); 
        double addMatrix[][] = new double[addrow][addcol];
 
        
 
        for(int i = 0;i<addrow;i++) { 
            for(int j = 0;j<addcol;j++) { 
                double ad = scan.nextDouble(); 
                addMatrix[i][j] = ad;
            } 
        } 
        Matrix add = new Matrix(addMatrix); 
        double[][] result1 = new double[row][col];
        
        result1 = matrix.add(add);
        Matrix result = new Matrix(result1);
        result.display();
        
//        //mul
        System.out.println("---------Matrix: multiple---------");
        int mulrow = scan.nextInt();
        int mulcol = scan.nextInt(); 
        double mulMatrix[][] = new double[mulrow][mulcol];    
 
        for(int i = 0;i<mulrow;i++) { 
            for(int j = 0;j<mulcol;j++) { 
                double mu = scan.nextDouble();
                mulMatrix[i][j] = mu;
            } 
        }
         Matrix mul = new Matrix(mulMatrix); 
         //System.out.print(matrix.add(add));
        double[][] result2 = new double[mulrow][col];
         result2 = matrix.multi(mul);
         result = new Matrix(result2);
         result.display();
        

         
         int size = scan.nextInt();
         System.out.println("size:"+size);
         double[] vecdata = new double[size];
         for(int i=0;i<size;++i){
            vecdata[i]=scan.nextDouble();
         }
        Vector v = new Vector(vecdata);
        System.out.println("---------Vector: display---------");
        
        v.display();
        System.out.println("---------Vector: setValue---------");
        int sindex = scan.nextInt();
        double svalue = scan.nextDouble();
        v.setValue(sindex, svalue);
        System.out.println("The value of index:"+sindex);
        System.out.println(v.getValue(sindex));
        System.out.println("---------Vector: add---------");
         double[] adddata = new double[v.getSize()];
         for(int i=0;i<v.getSize();++i){
             adddata[i] = scan.nextDouble();
         }
        Vector addvec = new Vector(adddata);
        double[] resultadd = v.add(addvec);
        System.out.println(Arrays.toString(resultadd));
        System.out.println("---------Vector: dot---------");
        double[] dotdata = new double[v.getSize()];
        for(int i=0;i<v.getSize();++i){
            dotdata[i] = scan.nextDouble();
        }
       Vector dotvec = new Vector(dotdata);
       double dotresult = v.dot(dotvec);
       System.out.println("The result of dot:"+dotresult);

       System.out.println("---------MathUtils---------");
       UnmodifiableMatrix unmodifmatrix =  MathUtils.getUnmodifiableMatrix(matrix);
       UnmodifiableVector unmodifvector =  MathUtils.getUnmodifiableVector(v);
       System.out.println("Create unmodifiable class success!");
    //    System.out.println("---------UnmodifiableClass---------");




 
 }
 
}
