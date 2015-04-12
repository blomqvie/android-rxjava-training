#Android RxJava Introduction#

A small sample app for learning RxJava usage in Android.

## Get started

This application is divided into 3 tasks, aimed to implement Google image search with RxJava. HEAD commit contains all solutions, so please don't look at the code right away :)

### Input handling

Start by checking out the first task: `git checkout bcff30`

Implement `input` function in `AppObservables` class following instructions in code. Once you're done, you should see you input reflected to a view below the text field.

You can check out the solution by checking out commit fd03d1

### Networking and response parsing

This is probably the most difficult task in this exercise. It goes a bit deeper into RxJava and utilizes more advanced features.

To start, checkout out commit 7c5a35

The goal is again to implement a function in `AppObservables` - this time it's called `pictures`. As a result, you should see a list of pictures based on entered input which updates when input changes.

If the instructions are not clear enough or the task feels too difficult, solutions can be found in commit c62fb7.

### Side-effects in action

Last task is easy and straightforward. The goal is to create more side effects to UI by showing progress bar when image search is ongoing.

Checkout commit 3dbfe0 and implement `AppObservables.doWhenSearching` and you should have progress bar spinning during networking!

Solution can be found in commit fb7194

Have fun!
