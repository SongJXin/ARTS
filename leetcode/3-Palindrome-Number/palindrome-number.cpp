//Runtime: 60 ms, faster than 82.57% of C++ online submissions for Palindrome Number.
//Memory Usage: 17.5 MB, less than 97.56% of C++ online submissions for Palindrome Number.
class Solution {
public:
    bool isPalindrome(int x) {
        if (x < 0){
            return false;
        }
        deque<int> inter;
        while(x > 0){
            inter.push_back(x % 10);
            x = x / 10;
        }
        while(! inter.empty()){
            if (inter.front() != inter.back()){
                return false;
            }
            inter.pop_back();
            if (! inter.empty()){
                inter.pop_front();
            }
        }
        return true;
    }
};