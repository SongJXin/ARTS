//Runtime: 28 ms, faster than 90.85% of C++ online submissions for Roman to Integer.
//Memory Usage: 8.3 MB, less than 99.85% of C++ online submissions for Roman to Integer.
class Solution {
public:
    int romanToInt(string s) {
        int result = 0;
        for(int i = 0 ; i < s.length();i ++){
            if(s[i]=='I'){
                if (i != s.length() - 1 && s[i + 1]=='V'){
                    result += 4;
                    i++;
                }else if ( i != s.length() - 1 && s[i + 1] == 'X'){
                    result += 9;
                    i++;
                }else{
                    result++;
                }
            }else if(s[i] == 'X'){
                if (i != s.length() - 1 && s[i+1] == 'L'){
                    result +=40;
                    i++;
                }else if (i != s.length() - 1 && s[i+1] == 'C'){
                    result += 90;
                    i++;
                }else{
                    result += 10;
                }
            }else if(s[i] == 'C'){
                if (i != s.length() - 1 && s[i+1] == 'D'){
                    result += 400;
                    i ++;
                }else if (i != s.length() -1 && s[i+1] == 'M'){
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
};