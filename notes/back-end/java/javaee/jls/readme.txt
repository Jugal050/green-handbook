《The Java® Language Specification Java SE 8 Edtion》: Java8语言规范 	// start 2020-9-12 09:41:13

Preface to the Java SE 8 Edition xix

	1 Introduction 1 								// done 2020-9-12 17:41:04
		1.1 Organization of the Specification 2
		1.2 Example Programs 6
		1.3 Notation 6
		1.4 Relationship to Predefined Classes and Interfaces 7
		1.5 Feedback 7
		1.6 References 7

	2 Grammars 9
		2.1 Context-Free Grammars 9
		2.2 The Lexical Grammar 9
		2.3 The Syntactic Grammar 10
		2.4 Grammar Notation 10

	3 Lexical Structure 15
		3.1 Unicode 15
		3.2 Lexical Translations 16
		3.3 Unicode Escapes 17
		3.4 Line Terminators 19
		3.5 Input Elements and Tokens 19
		3.6 White Space 20
		3.7 Comments 21
		3.8 Identifiers 22
		3.9 Keywords 24
		3.10 Literals 24
			3.10.1 Integer Literals 25
			3.10.2 Floating-Point Literals 31
			3.10.3 Boolean Literals 34
			3.10.4 Character Literals 34
			3.10.5 String Literals 35
			3.10.6 Escape Sequences for Character and String Literals 37
			3.10.7 The Null Literal 38
		3.11 Separators 39
		3.12 Operators 39

	4 Types, Values, and Variables 41
		4.1 The Kinds of Types and Values 41
		4.2 Primitive Types and Values 42
			4.2.1 Integral Types and Values 43
			4.2.2 Integer Operations 43
			4.2.3 Floating-Point Types, Formats, and Values 45
			4.2.4 Floating-Point Operations 48
			4.2.5 The boolean Type and boolean Values 51
		4.3 Reference Types and Values 52
			4.3.1 Objects 53
			4.3.2 The Class Object 56
			4.3.3 The Class String 56
			4.3.4 When Reference Types Are the Same 57
		4.4 Type Variables 57
		4.5 Parameterized Types 59
			4.5.1 Type Arguments of Parameterized Types 60
			4.5.2 Members and Constructors of Parameterized Types 63
		4.6 Type Erasure 64
		4.7 Reifiable Types 65
		4.8 Raw Types 66
		4.9 Intersection Types 70
		4.10 Subtyping 71
			4.10.1 Subtyping among Primitive Types 71
			4.10.2 Subtyping among Class and Interface Types 72
			4.10.3 Subtyping among Array Types 73
			4.10.4 Least Upper Bound 73
		4.11 Where Types Are Used 76
		4.12 Variables 80
			4.12.1 Variables of Primitive Type 81
			4.12.2 Variables of Reference Type 81
			4.12.3 Kinds of Variables 83
			4.12.4 final Variables 85
			4.12.5 Initial Values of Variables 87
			4.12.6 Types, Classes, and Interfaces 88

	5 Conversions and Contexts 93
		5.1 Kinds of Conversion 96
			5.1.1 Identity Conversion 96
			5.1.2 Widening Primitive Conversion 96
			5.1.3 Narrowing Primitive Conversion 98
			5.1.4 Widening and Narrowing Primitive Conversion 101
			5.1.5 Widening Reference Conversion 101
			5.1.6 Narrowing Reference Conversion 101
			5.1.7 Boxing Conversion 102
			5.1.8 Unboxing Conversion 104
			5.1.9 Unchecked Conversion 105
			5.1.10 Capture Conversion 105
			5.1.11 String Conversion 107
			5.1.12 Forbidden Conversions 108
			5.1.13 Value Set Conversion 108
		5.2 Assignment Contexts 109
		5.3 Invocation Contexts 114
		5.4 String Contexts 116
		5.5 Casting Contexts 116
			5.5.1 Reference Type Casting 120
			5.5.2 Checked Casts and Unchecked Casts 124
			5.5.3 Checked Casts at Run Time 125
		5.6 Numeric Contexts 127
			5.6.1 Unary Numeric Promotion 127
			5.6.2 Binary Numeric Promotion 128

	6 Names 131
		6.1 Declarations 132
		6.2 Names and Identifiers 139
		6.3 Scope of a Declaration 141
		6.4 Shadowing and Obscuring 144
			6.4.1 Shadowing 146
			6.4.2 Obscuring 149
		6.5 Determining the Meaning of a Name 150
			6.5.1 Syntactic Classification of a Name According to Context 151
			6.5.2 Reclassification of Contextually Ambiguous Names 154
			6.5.3 Meaning of Package Names 156
				6.5.3.1 Simple Package Names 157
				6.5.3.2 Qualified Package Names 157
			6.5.4 Meaning of PackageOrTypeNames 157
				6.5.4.1 Simple PackageOrTypeNames 157
				6.5.4.2 Qualified PackageOrTypeNames 157
			6.5.5 Meaning of Type Names 157
				6.5.5.1 Simple Type Names 158
				6.5.5.2 Qualified Type Names 158
			6.5.6 Meaning of Expression Names 158
				6.5.6.1 Simple Expression Names 158
				6.5.6.2 Qualified Expression Names 159
			6.5.7 Meaning of Method Names 162
				6.5.7.1 Simple Method Names 162
		6.6 Access Control 163
			6.6.1 Determining Accessibility 164
			6.6.2 Details on protected Access 168
				6.6.2.1 Access to a protected Member 169
				6.6.2.2 Qualified Access to a protected Constructor 169
		6.7 Fully Qualified Names and Canonical Names 171

	7 Packages 175
		7.1 Package Members 175
		7.2 Host Support for Packages 177
		7.3 Compilation Units 179
		7.4 Package Declarations 180
			7.4.1 Named Packages 180
			7.4.2 Unnamed Packages 181
			7.4.3 Observability of a Package 181
		7.5 Import Declarations 182
			7.5.1 Single-Type-Import Declarations 182
			7.5.2 Type-Import-on-Demand Declarations 185
			7.5.3 Single-Static-Import Declarations 186
			7.5.4 Static-Import-on-Demand Declarations 186
		7.6 Top Level Type Declarations 187

	8 Classes 191

		8.1 Class Declarations 193
			8.1.1 Class Modifiers 193
				8.1.1.1 abstract Classes 194
				8.1.1.2 final Classes 196
				8.1.1.3 strictfp Classes 196
			8.1.2 Generic Classes and Type Parameters 196
			8.1.3 Inner Classes and Enclosing Instances 199
			8.1.4 Superclasses and Subclasses 202
			8.1.5 Superinterfaces 204
			8.1.6 Class Body and Member Declarations 207

		8.2 Class Members 208

		8.3 Field Declarations 213
			8.3.1 Field Modifiers 217
				8.3.1.1 static Fields 218
				8.3.1.2 final Fields 221
				8.3.1.3 transient Fields 221
				8.3.1.4 volatile Fields 222
			8.3.2 Field Initialization 223
			8.3.3 Forward References During Field Initialization 224

		8.4 Method Declarations 227

			8.4.1 Formal Parameters 228
			8.4.2 Method Signature 232
			8.4.3 Method Modifiers 233
				8.4.3.1 abstract Methods 234
				8.4.3.2 static Methods 236
				8.4.3.3 final Methods 236
				8.4.3.4 native Methods 237
				8.4.3.5 strictfp Methods 237
				8.4.3.6 synchronized Methods 238
			8.4.4 Generic Methods 239
			8.4.5 Method Result 240
			8.4.6 Method Throws 240
			8.4.7 Method Body 242
			8.4.8 Inheritance, Overriding, and Hiding 243
				8.4.8.1 Overriding (by Instance Methods) 243
				8.4.8.2 Hiding (by Class Methods) 247
				8.4.8.3 Requirements in Overriding and Hiding 248
				8.4.8.4 Inheriting Methods with Override-Equivalent Signatures 252
			8.4.9 Overloading 253

		8.5 Member Type Declarations 256
			8.5.1 Static Member Type Declarations 257

		8.6 Instance Initializers 257
		
		8.7 Static Initializers 258
		
		8.8 Constructor Declarations 258
			8.8.1 Formal Parameters 259
			8.8.2 Constructor Signature 260
			8.8.3 Constructor Modifiers 260
			8.8.4 Generic Constructors 261
			8.8.5 Constructor Throws 262
			8.8.6 The Type of a Constructor 262
			8.8.7 Constructor Body 262
				8.8.7.1 Explicit Constructor Invocations 263
			8.8.8 Constructor Overloading 267
			8.8.9 Default Constructor 267
			8.8.10 Preventing Instantiation of a Class 268

		8.9 Enum Types 269
			8.9.1 Enum Constants 270
			8.9.2 Enum Body Declarations 271
			8.9.3 Enum Members 273

	9 Interfaces 279

		9.1 Interface Declarations 280
			9.1.1 Interface Modifiers 280
				9.1.1.1 abstract Interfaces 281
				9.1.1.2 strictfp Interfaces 281
			9.1.2 Generic Interfaces and Type Parameters 281
			9.1.3 Superinterfaces and Subinterfaces 282
			9.1.4 Interface Body and Member Declarations 284
		9.2 Interface Members 284
		9.3 Field (Constant) Declarations 285
			9.3.1 Initialization of Fields in Interfaces 287
		9.4 Method Declarations 288
			9.4.1 Inheritance and Overriding 289
				9.4.1.1 Overriding (by Instance Methods) 290
				9.4.1.2 Requirements in Overriding 291
				9.4.1.3 Inheriting Methods with Override-Equivalent Signatures 291
			9.4.2 Overloading 292
			9.4.3 Interface Method Body 293
		9.5 Member Type Declarations 293
		9.6 Annotation Types 294
			9.6.1 Annotation Type Elements 295
			9.6.2 Defaults for Annotation Type Elements 299
			9.6.3 Repeatable Annotation Types 300
			9.6.4 Predefined Annotation Types 304
				9.6.4.1 @Target 304
				9.6.4.2 @Retention 305
				9.6.4.3 @Inherited 306
				9.6.4.4 @Override 306
				9.6.4.5 @SuppressWarnings 307
				9.6.4.6 @Deprecated 308
				9.6.4.7 @SafeVarargs 309
				9.6.4.8 @Repeatable 310
				9.6.4.9 @FunctionalInterface 310
		9.7 Annotations 310
			9.7.1 Normal Annotations 311
			9.7.2 Marker Annotations 313
			9.7.3 Single-Element Annotations 314
			9.7.4 Where Annotations May Appear 315
			9.7.5 Multiple Annotations of the Same Type 320
		9.8 Functional Interfaces 321
		9.9 Function Types 325

	10 Arrays 331
		10.1 Array Types 332
		10.2 Array Variables 332
		10.3 Array Creation 335
		10.4 Array Access 335
		10.5 Array Store Exception 336
		10.6 Array Initializers 337
		10.7 Array Members 339
		10.8 Class Objects for Arrays 340
		10.9 An Array of Characters Is Not a String 342
		
	11 Exceptions 343
		11.1 The Kinds and Causes of Exceptions 344
			11.1.1 The Kinds of Exceptions 344
			11.1.2 The Causes of Exceptions 345
			11.1.3 Asynchronous Exceptions 346
		11.2 Compile-Time Checking of Exceptions 347
			11.2.1 Exception Analysis of Expressions 348
			11.2.2 Exception Analysis of Statements 349
			11.2.3 Exception Checking 350
		11.3 Run-Time Handling of an Exception 352
		
	12 Execution 357
		12.1 Java Virtual Machine Startup 357
			12.1.1 Load the Class Test 358
			12.1.2 Link Test: Verify, Prepare, (Optionally) Resolve 358
			12.1.3 Initialize Test: Execute Initializers 359
			12.1.4 Invoke Test.main 360
		12.2 Loading of Classes and Interfaces 360
			12.2.1 The Loading Process 361
		12.3 Linking of Classes and Interfaces 362
			12.3.1 Verification of the Binary Representation 362
			12.3.2 Preparation of a Class or Interface Type 363
			12.3.3 Resolution of Symbolic References 363
		12.4 Initialization of Classes and Interfaces 364
			12.4.1 When Initialization Occurs 365
			12.4.2 Detailed Initialization Procedure 367
		12.5 Creation of New Class Instances 370
		12.6 Finalization of Class Instances 373
			12.6.1 Implementing Finalization 375
			12.6.2 Interaction with the Memory Model 376
		12.7 Unloading of Classes and Interfaces 378
		12.8 Program Exit 379
		
	13 Binary Compatibility 381

		13.1 The Form of a Binary 382

		13.2 What Binary Compatibility Is and Is Not 388

		13.3 Evolution of Packages 389

		13.4 Evolution of Classes 389
			13.4.1 abstract Classes 389
			13.4.2 final Classes 389
			13.4.3 public Classes 390
			13.4.4 Superclasses and Superinterfaces 390
			13.4.5 Class Type Parameters 391
			13.4.6 Class Body and Member Declarations 392
			13.4.7 Access to Members and Constructors 393
			13.4.8 Field Declarations 394
			13.4.9 final Fields and static Constant Variables 397
			13.4.10 static Fields 399
			13.4.11 transient Fields 399
			13.4.12 Method and Constructor Declarations 400
			13.4.13 Method and Constructor Type Parameters 400
			13.4.14 Method and Constructor Formal Parameters 401
			13.4.15 Method Result Type 402
			13.4.16 abstract Methods 402
			13.4.17 final Methods 403
			13.4.18 native Methods 403
			13.4.19 static Methods 404
			13.4.20 synchronized Methods 404
			13.4.21 Method and Constructor Throws 404
			13.4.22 Method and Constructor Body 404
			13.4.23 Method and Constructor Overloading 405
			13.4.24 Method Overriding 406
			13.4.25 Static Initializers 406
			13.4.26 Evolution of Enums 406

		13.5 Evolution of Interfaces 406
			13.5.1 public Interfaces 406
			13.5.2 Superinterfaces 407
			13.5.3 Interface Members 407
			13.5.4 Interface Type Parameters 407
			13.5.5 Field Declarations 408
			13.5.6 Interface Method Declarations 408
			13.5.7 Evolution of Annotation Types 409
		
	14 Blocks and Statements 411
		14.1 Normal and Abrupt Completion of Statements 411
		14.2 Blocks 413
		14.3 Local Class Declarations 413
		14.4 Local Variable Declaration Statements 414
			14.4.1 Local Variable Declarators and Types 415
			14.4.2 Execution of Local Variable Declarations 416
		14.5 Statements 416
		14.6 The Empty Statement 418
		14.7 Labeled Statements 419
		14.8 Expression Statements 420
		14.9 The if Statement 421
			14.9.1 The if-then Statement 422
			14.9.2 The if-then-else Statement 422
		14.10 The assert Statement 422
		14.11 The switch Statement 425
		14.12 The while Statement 429
			14.12.1 Abrupt Completion of while Statement 430
		14.13 The do Statement 431
			14.13.1 Abrupt Completion of do Statement 431
		14.14 The for Statement 433
			14.14.1 The basic for Statement 433
				14.14.1.1 Initialization of for Statement 434
				14.14.1.2 Iteration of for Statement 434
				14.14.1.3 Abrupt Completion of for Statement 435
			14.14.2 The enhanced for statement 436
		14.15 The break Statement 438
		14.16 The continue Statement 440
		14.17 The return Statement 442
		14.18 The throw Statement 444
		14.19 The synchronized Statement 446
		14.20 The try statement 447
			14.20.1 Execution of try-catch 450
			14.20.2 Execution of try-finally and try-catch-finally 452
			14.20.3 try-with-resources 454
				14.20.3.1 Basic try-with-resources 455
				14.20.3.2 Extended try-with-resources 458
		14.21 Unreachable Statements 458
		
	15 Expressions 465
		15.1 Evaluation, Denotation, and Result 465
		15.2 Forms of Expressions 466
		15.3 Type of an Expression 467
		15.4 FP-strict Expressions 468
		15.5 Expressions and Run-Time Checks 468
		15.6 Normal and Abrupt Completion of Evaluation 470
		15.7 Evaluation Order 472
			15.7.1 Evaluate Left-Hand Operand First 472
			15.7.2 Evaluate Operands before Operation 474
			15.7.3 Evaluation Respects Parentheses and Precedence 475
			15.7.4 Argument Lists are Evaluated Left-to-Right 476
			15.7.5 Evaluation Order for Other Expressions 477
		15.8 Primary Expressions 477
			15.8.1 Lexical Literals 478
			15.8.2 Class Literals 479
			15.8.3 this 480
			15.8.4 Qualified this 481
			15.8.5 Parenthesized Expressions 481
		15.9 Class Instance Creation Expressions 482
			15.9.1 Determining the Class being Instantiated 484
			15.9.2 Determining Enclosing Instances 486
			15.9.3 Choosing the Constructor and its Arguments 487
			15.9.4 Run-Time Evaluation of Class Instance Creation Expressions 490
			15.9.5 Anonymous Class Declarations 491
				15.9.5.1 Anonymous Constructors 491
		15.10 Array Creation and Access Expressions 493
			15.10.1 Array Creation Expressions 493
			15.10.2 Run-Time Evaluation of Array Creation Expressions 494
			15.10.3 Array Access Expressions 497
			15.10.4 Run-Time Evaluation of Array Access Expressions 498
		15.11 Field Access Expressions 500
			15.11.1 Field Access Using a Primary 500
			15.11.2 Accessing Superclass Members using super 503
		15.12 Method Invocation Expressions 505
			15.12.1 Compile-Time Step 1: Determine Class or Interface to Search 506
			15.12.2 Compile-Time Step 2: Determine Method Signature 509
				15.12.2.1 Identify Potentially Applicable Methods 515
				15.12.2.2 Phase 1: Identify Matching Arity Methods Applicable by Strict Invocation 517
				15.12.2.3 Phase 2: Identify Matching Arity Methods Applicable by Loose Invocation 519
				15.12.2.4 Phase 3: Identify Methods Applicable by Variable Arity Invocation 519
				15.12.2.5 Choosing the Most Specific Method 520
				15.12.2.6 Method Invocation Type 523
			15.12.3 Compile-Time Step 3: Is the Chosen Method Appropriate? 523
			15.12.4 Run-Time Evaluation of Method Invocation 526
				15.12.4.1 Compute Target Reference (If Necessary) 527
				15.12.4.2 Evaluate Arguments 528
				15.12.4.3 Check Accessibility of Type and Method 529
				15.12.4.4 Locate Method to Invoke 530
				15.12.4.5 Create Frame, Synchronize, Transfer Control 534
		15.13 Method Reference Expressions 536
			15.13.1 Compile-Time Declaration of a Method Reference 539
			15.13.2 Type of a Method Reference 544
			15.13.3 Run-Time Evaluation of Method References 546
		15.14 Postfix Expressions 549
			15.14.1 Expression Names 550
			15.14.2 Postfix Increment Operator ++ 550
			15.14.3 Postfix Decrement Operator -- 551
		15.15 Unary Operators 551
			15.15.1 Prefix Increment Operator ++ 553
			15.15.2 Prefix Decrement Operator -- 553
			15.15.3 Unary Plus Operator + 554
			15.15.4 Unary Minus Operator - 554
			15.15.5 Bitwise Complement Operator ~ 555
			15.15.6 Logical Complement Operator ! 555
		15.16 Cast Expressions 556
		15.17 Multiplicative Operators 557
			15.17.1 Multiplication Operator * 558
			15.17.2 Division Operator / 559
			15.17.3 Remainder Operator % 561
		15.18 Additive Operators 563
			15.18.1 String Concatenation Operator + 564
			15.18.2 Additive Operators (+ and -) for Numeric Types 566
		15.19 Shift Operators 568
		15.20 Relational Operators 569
			15.20.1 Numerical Comparison Operators <, <=, >, and >= 570
			15.20.2 Type Comparison Operator instanceof 571
		15.21 Equality Operators 572
			15.21.1 Numerical Equality Operators == and != 573
			15.21.2 Boolean Equality Operators == and != 574
			15.21.3 Reference Equality Operators == and != 574
		15.22 Bitwise and Logical Operators 575
			15.22.1 Integer Bitwise Operators &, ^, and | 575
			15.22.2 Boolean Logical Operators &, ^, and | 576
		15.23 Conditional-And Operator && 577
		15.24 Conditional-Or Operator || 577
		15.25 Conditional Operator ? : 578
			15.25.1 Boolean Conditional Expressions 586
			15.25.2 Numeric Conditional Expressions 586
			15.25.3 Reference Conditional Expressions 587
		15.26 Assignment Operators 588
			15.26.1 Simple Assignment Operator = 589
			15.26.2 Compound Assignment Operators 595
		15.27 Lambda Expressions 601
			15.27.1 Lambda Parameters 603
			15.27.2 Lambda Body 606
			15.27.3 Type of a Lambda Expression 609
			15.27.4 Run-Time Evaluation of Lambda Expressions 611
		15.28 Constant Expressions 612
		
	16 Definite Assignment 615
		16.1 Definite Assignment and Expressions 621
			16.1.1 Boolean Constant Expressions 621
			16.1.2 Conditional-And Operator && 621
			16.1.3 Conditional-Or Operator || 622
			16.1.4 Logical Complement Operator ! 622
			16.1.5 Conditional Operator ? : 622
			16.1.6 Conditional Operator ? : 623
			16.1.7 Other Expressions of Type boolean 623
			16.1.8 Assignment Expressions 623
			16.1.9 Operators ++ and -- 624
			16.1.10 Other Expressions 624
		16.2 Definite Assignment and Statements 625
			16.2.1 Empty Statements 625
			16.2.2 Blocks 625
			16.2.3 Local Class Declaration Statements 627
			16.2.4 Local Variable Declaration Statements 627
			16.2.5 Labeled Statements 627
			16.2.6 Expression Statements 628
			16.2.7 if Statements 628
			16.2.8 assert Statements 628
			16.2.9 switch Statements 629
			16.2.10 while Statements 629
			16.2.11 do Statements 630
			16.2.12 for Statements 630
				16.2.12.1 Initialization Part of for Statement 631
				16.2.12.2 Incrementation Part of for Statement 631
			16.2.13 break, continue, return, and throw Statements 632
			16.2.14 synchronized Statements 632
			16.2.15 try Statements 632
		16.3 Definite Assignment and Parameters 634
		16.4 Definite Assignment and Array Initializers 634
		16.5 Definite Assignment and Enum Constants 634
		16.6 Definite Assignment and Anonymous Classes 635
		16.7 Definite Assignment and Member Types 635
		16.8 Definite Assignment and Static Initializers 636
		16.9 Definite Assignment, Constructors, and Instance Initializers 636
		
	17 Threads and Locks 639
		17.1 Synchronization 640
		17.2 Wait Sets and Notification 640
			17.2.1 Wait 641
			17.2.2 Notification 642
			17.2.3 Interruptions 643
			17.2.4 Interactions of Waits, Notification, and Interruption 643
		17.3 Sleep and Yield 644
		17.4 Memory Model 645
			17.4.1 Shared Variables 648
			17.4.2 Actions 648
			17.4.3 Programs and Program Order 649
			17.4.4 Synchronization Order 650
			17.4.5 Happens-before Order 651
			17.4.6 Executions 654
			17.4.7 Well-Formed Executions 655
			17.4.8 Executions and Causality Requirements 655
			17.4.9 Observable Behavior and Nonterminating Executions 658
		17.5 final Field Semantics 660
			17.5.1 Semantics of final Fields 662
			17.5.2 Reading final Fields During Construction 662
			17.5.3 Subsequent Modification of final Fields 663
			17.5.4 Write-Protected Fields 664
		17.6 Word Tearing 665
		17.7 Non-Atomic Treatment of double and long 666
		
	18 Type Inference 667
		18.1 Concepts and Notation 668
			18.1.1 Inference Variables 668
			18.1.2 Constraint Formulas 669
			18.1.3 Bounds 669
		18.2 Reduction 671
			18.2.1 Expression Compatibility Constraints 671
			18.2.2 Type Compatibility Constraints 676
			18.2.3 Subtyping Constraints 677
			18.2.4 Type Equality Constraints 678
			18.2.5 Checked Exception Constraints 679
		18.3 Incorporation 681
			18.3.1 Complementary Pairs of Bounds 682
			18.3.2 Bounds Involving Capture Conversion 683
		18.4 Resolution 684
		18.5 Uses of Inference 686
			18.5.1 Invocation Applicability Inference 686
			18.5.2 Invocation Type Inference 688
			18.5.3 Functional Interface Parameterization Inference 694
			18.5.4 More Specific Method Inference 695

	19 Syntax 699
		Index 725
		A Limited License Grant 765