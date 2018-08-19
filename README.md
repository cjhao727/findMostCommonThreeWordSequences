### Find Most Common Three Word Sequences

#### Analysis
According to the instruction, I have listed out the key points in my opinion.

1. Input and output  
   - input: command line args or stdin

2. core logic

   - Find top 100 common three word sequences
   - Ignores punctuation, line endings, and is case insensitive
   - thoughts: 
   
     how do I define "three word sequences". "Top 100" means, I need to sort the sequences.
     What if there are two sequences have same occurrences?
     
3. Need to handle large file and KPI need to be considered.

#### Algorithm of my solution

This is kind of similar to one of the projects I did before. I need to read lots of files and parse them by following certain programming language syntax rules.

- Data structure

   - HashMap:
   
        "three word sequences" should be the key and occurrences should be the value.
   
   - PriorityQueue (still thinking)
   
        use to output "Top 100" items in the HashMap.
        
- solution

    1. read test.txt file as input.
    2. trim the input. Convert all tokens to lowercase and filer out punctuation, whitespace.
    3. Traversal the trimmed input to fill the HashMap.
    4. Sort items of the HashMap and out put Top 100 of them.
    5. Time complexity should be O(nlogn) and space complexity should be O(n);

#### start coding

- Updates:

    - Inorder to handle large file, replace HashMap by concurrentHashMap;
    - Since PriorityQueue poll method's time complexity is as same as stream sorted. Will just implement stream.
    
- Passed all 3 tests
- Will refactor the code to implement best code practice.