Dataplant Runner
================

Sample project with my first real deployment idea for new DataPlant processes.

Overview
========

This is an overly simplistic example of how we could use very basic
reflection combined with a trait for any data process to be able to
handle deployment. The idea here is that we have this common entry
point with a main that takes as arguments a fully-qualified class name and
then the array of other arguments. The first argument must be the object
you want to run everything else will be based to that object.

The object must use the DataProcess trait which defines an execute method.

The execute method will receive an `Array[String]` and do what it wants with them.

This combined with an executable jar (either through java -jar runner.jar) or
by pre-pending a shell script onto the jar. We can debate the merits of this.

My suggestion would be that this jar could also contain a common logback.xml
and any other common resources (I'll get in trouble here but graphite config,
sentry config, etc).

The end result is the ability to do something like:

./runner.jar com.pragmafs.dataplant.FIXLogLoader --date 20150400g

I have to work out the details of using to run classes in process that aren't apart
of runner.jar but on the surface I appreciate this approach for its simplicity of
deployment as well as the uniformity that data processes will have by adhering to the
DataProcess trait. Finally, it begins the unifying of common resources (like logging, etc)

Its not perfect but its a good idea that needs some vetting.

