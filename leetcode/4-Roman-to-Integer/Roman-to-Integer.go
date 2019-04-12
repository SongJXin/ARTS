//Runtime: 16 ms, faster than 100.00% of Go online submissions for Roman to Integer.
//Memory Usage: 3 MB, less than 22.00% of Go online submissions for Roman to Integer.
func romanToInt(s string) int {
	result := 0
	for i := 0; i < len(s); i++{
        if(s[i]=='I'){
            if (i != len(s) - 1 && s[i + 1]=='V'){
                result += 4;
                i++;
            }else if ( i != len(s) - 1 && s[i + 1] == 'X'){
                result += 9;
                i++;
            }else{
                result++;
            }
        }else if(s[i] == 'X'){
            if (i != len(s) - 1 && s[i+1] == 'L'){
                result +=40;
                i++;
            }else if (i != len(s) - 1 && s[i+1] == 'C'){
                result += 90;
                i++;
            }else{
                result += 10;
            }
        }else if(s[i] == 'C'){
            if (i != len(s) - 1 && s[i+1] == 'D'){
                result += 400;
                i ++;
            }else if (i != len(s) -1 && s[i+1] == 'M'){
                result += 900;
                i++;
            }else{
                result += 100;
            }
        }else if(s[i]=='V'){
            result += 5;
        }else if(s[i]=='L'){
            result += 50;
        }else if(s[i]=='D'){
            result += 500;
        }else if (s[i]=='M'){
            result += 1000;
        }
    }
    return result;
}