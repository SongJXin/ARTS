//Runtime: 8 ms, faster than 97.82% of C++ online submissions for Merge Two Sorted Lists.
//Memory Usage: 9 MB, less than 64.22% of C++ online submissions for Merge Two Sorted Lists.

/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode(int x) : val(x), next(NULL) {}
 * };
 */
class Solution {
public:
    ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
	ListNode *l;
	if (l1 == NULL) {
		return l2;
	}
	if (l2 == NULL) {
		return l1;
	}
	if (l1->val > l2->val) {
		l = l2;
		l2 = l1;
		l1 = l;
	}
	l = l1;
	while (l2 != NULL) {
		if (l1->next == NULL) {
			l1->next = l2;
			break;
		}
		while (l2 != NULL &&l2->val <= l1->next->val) {
			ListNode *l3 = l1->next;
			l1->next = l2;
			l2 = l2->next;
			l1->next->next = l3;
		}
		l1 = l1->next;
		
	}
	return l;
}
    
};