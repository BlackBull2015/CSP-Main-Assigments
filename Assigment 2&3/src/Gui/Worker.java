//package Gui;
//
//
//public class Worker{
//
//
//    new SwingWorker<Integer, String>(){
//
////runs on a background thread.
//        protected Integer doInBackground()throws Exception{
//            Integer fileCount=0;
//            for(File file:getListOfFiles()){
//                publish(file.getName); //passes the name of the file to process()
//                scanFile(file); //just an example method
//                fileCount++;
//            }
//            return fileCount; //can be retrieved by calling get()
//        }
//
////runs on EDT, allowed to update gui
//    protected void process(String fileName){
//        //receives the name of the file from publish() and sets it on the textfield.
//        textField.setText("scanning file: "+fileName);
//    }
//
//    //runs on EDT, allowed to update gui
//    protected void done(){
//        try{
//            Integer fileCount=get(); //get() retrieves the return value from doInBackground()
//            textField.setText("scan complete "+fileCount+" files scanned");
//        }catch(Exception e){
//            //this is where you handle any exceptions that occurred in the
//            //doInBackground() method
//        }
//    }
//}.execute();
//        }
//}