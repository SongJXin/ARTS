//Runtime: 0 ms, faster than 100.00% of Java online submissions for Merge Two Sorted Lists.
//Memory Usage: 40 MB, less than 54.34% of Java online submissions for Merge Two Sorted Lists.

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
                if (l1==null){
            return l2;
        }
        if (l2 == null){
            return l1;
        }
        ListNode l;
        if (l1.val > l2.val){
            l = l1;
            l1 = l2;
            l2 = l;
        }
        l = l1;
        while(l2 != null){
            if(l1.next == null){
                l1.next = l2;
                break;
            }
            if(l2.val <= l1.next.val){
                ListNode l3 = l1.next;
                l1.next = l2;
                l2 = l2.next;
                l1.next.next=l3;
            }else{
                l1 = l1.next;
            }

        }
        return l;
    }
}