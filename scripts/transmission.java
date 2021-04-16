public static double Transmission(Mat mat, double d, double d2, double d3) {
        Mat mat2;
        Mat submat = mat.submat(376, 540, 337, 667);
        submat.convertTo(submat, 5); //Normalize submatrix
        List arrayList = new ArrayList();
        Core.split(submat, arrayList); //Split RGB channels
        Mat mat3 = (Mat) arrayList.get(0); //B
        Mat mat4 = (Mat) arrayList.get(1); //G
        Mat mat5 = (Mat) arrayList.get(2); //R
        int rows = mat3.rows();
        int cols = mat3.cols();
        Mat clone = mat3.clone();  //Copy
        for (int i = 0; i < submat.rows(); i++) {
            Mat mat6;
            Mat mat7;
            int i2 = 0;
            while (i2 < submat.cols()) {
                mat6 = mat4;
                mat2 = submat;
                mat7 = mat5;
                clone.put(i, i2, new double[]{Math.min(mat3.get(i, i2)[0], Math.min(mat4.get(i, i2)[0], mat5.get(i, i2)[0]))});
                i2++;
                mat4 = mat6;
                submat = mat2;
                mat5 = mat7;
            }
            mat2 = submat;
            mat7 = mat5;
            mat6 = mat4;
        }
        mat2 = submat;
        int intValue = Double.valueOf(Math.max(Math.max(((double) rows) * d, ((double) cols) * d), 3.0d)).intValue();
        double d4 = (double) intValue;
        Imgproc.erode(clone, clone, Imgproc.getStructuringElement(0, new Size(d4, d4), new Point(-1.0d, -1.0d)));
        Mat clone2 = clone.clone();
        Core.subtract(clone2, new Scalar(255.0d), clone2);
        Core.multiply(clone2, new Scalar(-1.0d), clone2);
        Core.divide(clone2, new Scalar(255.0d), clone2);
        submat = new Mat();
        Imgproc.cvtColor(mat2, submat, 7);
        Core.divide(submat, new Scalar(255.0d), submat);
        double d5 = Core.mean(GuidedImageFilter(submat, clone2, intValue * 4, d3)).val[0];
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("transmission ");
        stringBuilder.append(d5);
        printStream.println(stringBuilder.toString());
        return d5;
    }