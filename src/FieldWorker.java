class FieldWorker implements Runnable {

    @Override
    public void run() {
        Field.calculateShortestPath();
    }

}
