# PinBlockGenerator

1. The UI emulates a physical POS.
2. The app architecture is MVVM + CLEAN. Dagger-hilt is applied for Dependency Injection. 
3. The View layer (PosActivity) is dumb, it just serves as
   a container of UI elements. The PosViewModel instructs it to react.
4. The PAN is advised to be hardcoded as "1111222233334444". 
   As per https://www.investopedia.com/terms/p/primary-account-number-pan.asp and
   https://www.eftlab.com/knowledge-base/261-complete-list-of-pin-blocks-in-payments,
   The last digit is called the checksum number that should be excluded in the calculation.
   Hence, the 12 rightmost digits would be "122223333444".
5. The correctness of the calculation result had been verified against the online PIN BLOCK
   examples and calculator.
6. A local unit test case is available to test the algorithm.

Note that the reference doc ambiguously states the random fill is either "10" to "15" or 
X'0' to X'F'. As per http://icma.com/wp-content/uploads/2015/07/PinBlockFormats_SE1-15CM.pdf, 
it should be "0" to "15" (0x0 - 0xF), while some other docs say 0xA - 0xF. The app is using
0x0 - 0xF.