import java.util.*;

public class Main {
    public String randomExp(int digit, int number){
        String new_exp = "";
        Random rand = new Random();
        int maxx=0;
        maxx = (int)Math.pow(10,digit);
        int tmpnumber = rand.nextInt(maxx);
        new_exp += tmpnumber;
        int new_operator = 0;   //'+' 0, '-' 1
        for(int i=1;i<number;++i){
            new_operator = rand.nextInt(2);
            if(new_operator == 0){
                new_exp += "+";
            } else{
                new_exp += "-";
            }
            tmpnumber = rand.nextInt(maxx);
            new_exp += tmpnumber;
        }
        new_exp += "=";
        tmpnumber = rand.nextInt(maxx);
        new_exp += tmpnumber;
        return new_exp;
    }
    public static int play(){
        //测试GetAnswer函数的正确性
        // ArrayList<String> answer_list = new ArrayList<>();
        // answer_list = GetAnswer.getAnswer("6-9=0", 3, 2);
        // ListIterator<String> lit = answer_list.listIterator();
        // HashSet<String> ans_hash = new HashSet<String>(answer_list);
        // ArrayList<String> ans_with_no_duplicate = new ArrayList<String>(ans_hash);
        // System.out.println(ans_with_no_duplicate);
        // answer_list = GetAnswer.getAnswer("4+2=3", 2, 2);
        // System.out.println(answer_list);
        // answer_list = GetAnswer.getAnswer("75+11=27", 1, 1);
        // System.out.println(answer_list);
        System.out.println("\033[33m"+"-----------------------------------"+"\033[0m");
        System.out.println("\033[33m"+"Welcome to Moving Matches Games!"+"\033[0m");
        System.out.println("\033[33m"+"-----------------------------------"+"\033[0m");
        Scanner in = new Scanner(System.in);
        // in.nextLine();
        System.out.println("\033[33m"+"Input the maximum digit of numbers:"+"\033[0m");
        int max_digit = in.nextInt();
        System.out.println("\033[33m"+"Input the number of digits on the left of the equal sign:"+"\033[0m");
        int total_number = in.nextInt();
        System.out.println("\033[33m"+"Input question type number(1 is move, 2 is remove, 3 is add):"+"\033[0m");
        int game_mde = in.nextInt();
        System.out.println("\033[33m"+"Input the number of operated matches:"+"\033[0m");
        int move_matches_number = in.nextInt();  
        Main main_ctrl = new Main();
        String expression = main_ctrl.randomExp(max_digit, total_number);
        ArrayList<String> answer_list = new ArrayList<>();
        answer_list = GetAnswer.getAnswer(expression, game_mde,move_matches_number);
        while(answer_list.isEmpty()){
            expression = main_ctrl.randomExp(max_digit, total_number);
            answer_list = GetAnswer.getAnswer(expression, game_mde, move_matches_number);
        }
        System.out.println("\033[35m"+expression+"\033[0m");
        System.out.println("\033[33m"+"Input your answer: "+"\033[0m");
        String input_answer = "";
        in.nextLine();
        input_answer = in.nextLine();
        int is_find = 0;
        while(is_find == 0){
            if(input_answer.isEmpty()){
                System.out.println("Answer:"+ answer_list);
                is_find = 1;
            }else{
                ListIterator<String> lit = answer_list.listIterator();
                    while(lit.hasNext()){
                        String str = (String)lit.next();
                        if(str.equals(input_answer)){
                            System.out.println("\033[32m"+"Right answer!" + "\033[0m");
                            is_find = 1;
                        }
                    } 
            }
            if (is_find == 0){
                System.out.println("\033[31m" +"Wrong answer!" + "\033[0m");
                input_answer = in.nextLine();
            }
        }

        System.out.println("Enter 0 to exit; Enter 1 to restart.");
        
        String state = in.nextLine();
        int game_state =  Integer.parseInt(state);
        // in.close();
        return game_state;
        // return 0;
    }
    public static void main(String args[]){
            int is_play = 1;
            while(is_play == 1){
                is_play = Main.play();
            }
    }
}
