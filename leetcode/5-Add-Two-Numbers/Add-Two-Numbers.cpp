//Runtime: 28 ms, faster than 97.73% of C++ online submissions for Add Two Numbers.
//Memory Usage: 10.7 MB, less than 96.89% of C++ online submissions for Add Two Numbers.

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
    ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
		ListNode *lsum = new ListNode(0);
	bool carry = false;
	if (l1->next == NULL && l2->next != NULL) {
		ListNode *lnew = new ListNode(0);
		l1->next = lnew;
	}
	else if (l2->next == NULL && l1->next != NULL) {
		ListNode *lnew = new ListNode(0);
		l2->next = lnew;
	}
	int sum = 0;
	sum = l1->val + l2->val;
	if (sum >= 10) {
		carry = true;
		sum = sum % 10;
	}
    lsum -> val = sum;
	if (carry) {
		if (l1->next != NULL) {
			l1->next->val += 1;
		}
		else
		{
			ListNode *lnew = new ListNode(1);
			lsum->next = lnew;
			return lsum;
		}
	}
	if (l1->next != NULL) {
		lsum->next = addTwoNumbers(l1->next, l2->next);
	}
	else {
		lsum->next = NULL;
	}
	return lsum;
    }
};