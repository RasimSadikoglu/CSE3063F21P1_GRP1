package ObjectGeneration;

public class DataDebuger {
    private DataGenerator generator;

    // debug prints the DataGenerators data
    public DataDebuger(DataGenerator generator){
        this.generator = generator;
    }

    public void debugBell(){
        for (int i = 0; i < generator.getBellDataSize(); i++){
            System.out.print(generator.bellShape.get(i) + " ");
        }

        float scaleX = 1.0f;
        float scaleY = 0.1f;
        for (int i = 0; i < generator.getBellDataSize() * scaleY; i++){
            float yPos = 1 - ((float) i / (generator.getBellDataSize() * scaleY));
            System.out.println();
            for (int j = 0; j < generator.getBellDataSize() * scaleX; j++) {
                int arrPos = (int) ((float) j / scaleX);
                if (generator.bellShape.get(arrPos) >= yPos) {
                    System.out.print("*");
                } else {
                    System.out.print("-");
                }
            }
        }
    }
}
