if [[ -z "$1" ]]; then
    echo "Usage: ./run-tests test-number"
    exit 1
fi

if [ "$1" -eq 1 ]; then
    test_correct="super.correct.txt"
    test_test="super.test.txt"
elif [ "$1" -eq 2 ]; then
    test_correct="giant.correct.txt"
    test_test="giant.test.txt"
else
    echo "Test number must be either 1 or 2"
    exit 1
fi

echo "Testing..."

test_result="$(diff --strip-trailing-cr $test_correct <(java Main < $test_test) | head -n 1 | sed 's/c.*//')"

if [ -z $test_result ]; then
    echo "Passed"
else
    echo "Failed test:"
    awk "NR > $(($test_result*3 - 1))" $test_test | head -n 3
    echo "Expected output:"
    awk "NR == $test_result" $test_correct
fi
