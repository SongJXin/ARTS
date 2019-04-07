//Runtime: 144 ms, faster than 33.43% of C++ online submissions for Two Sum.
//Memory Usage: 9.3 MB, less than 98.81% of C++ online submissions for Two Sum.
class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        vector<int> result;
        for(int i = 0; i < nums.size(); i++){
            for (int j = i + 1; j < nums.size(); j++){
                if (target - nums[i] == nums[j]){
                    result.push_back(i);
                    result.push_back(j);
                    return result;
                } 
            }
        }
        return  result;
    }
};