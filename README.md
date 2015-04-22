#Android RxJava Introduction#

A small sample app for learning RxJava usage in Android.

## Get started

This application is divided into 3 tasks, aimed to implement Google image search with RxJava. HEAD commit contains all solutions, so please don't look at the code right away :)

### Input handling

Start by checking out the first task: `git checkout 95f5719`

Implement `input` function in `AppObservables` class following instructions in code. Once you're done, you should see you input reflected to a view below the text field.

You can check out the solution by checking out commit f3aa31c

### Networking and response parsing

This is probably the most difficult task in this exercise. It goes a bit deeper into RxJava and utilizes more advanced features.

To start, checkout out commit c0720c0

The goal is again to implement a function in `AppObservables` - this time it's called `pictures`. As a result, you should see a list of pictures based on entered input which updates when input changes.

If the instructions are not clear enough or the task feels too difficult, solutions can be found in commit 5de7492

### Side-effects in action

Last task is easy and straightforward. The goal is to create more side effects to UI by showing progress bar when image search is ongoing.

Checkout commit 9fc8357 and implement `AppObservables.doWhenSearching` and you should have progress bar spinning during networking!

Solution can be found in commit 6f80141

### UI feedback

The absolute final last task is also easy and straightforward. Since we're not firing off image searches for search terms that are too short, we'd like to indicate to the user that while they modify the search term and it's too short the search results on the screen are visually "grayed out" to indicate that the images on the screen are not for the current (invalid) search term.

Have fun!
