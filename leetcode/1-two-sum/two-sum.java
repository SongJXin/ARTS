//Runtime: 16 ms, faster than 41.95% of Java online submissions for Two Sum.
//Memory Usage: 38.4 MB, less than 51.90% of Java online submissions for Two Sum.
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0; i < nums.length;i ++){
            for(int j = i + 1; j < nums.length; j++){
                if (target - nums[i] == nums[j]) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result;
    }
}