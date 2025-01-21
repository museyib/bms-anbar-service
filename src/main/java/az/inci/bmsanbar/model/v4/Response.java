package az.inci.bmsanbar.model.v4;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response<T>
{
    public static final String NOT_FOUND_VALID_SHIPMENT = "Bu sənəd üzrə keçərli yükləmə tapılmadı!";
    public static final String NOT_VALID_SHIPMENT_DOC = "Yükləmə üçün keçərli sənəd deyil.";

    private int statusCode;
    private String systemMessage;
    private String developerMessage;
    private T data;


    public static <T> Response<T> getResultResponse(T result)
    {
        return Response.<T>builder()
                .statusCode(0)
                .data(result)
                .build();
    }

    public static <T> Response<T> getSuccessResponse()
    {
        return Response.<T>builder()
                .statusCode(0)
                .developerMessage("Uğurlu əməliyyat")
                .build();
    }

    public static <T> Response<T> getServerErrorResponse(String message)
    {
        return Response.<T>builder()
                .statusCode(1)
                .systemMessage(message)
                .developerMessage("Server xətası")
                .build();
    }

    public static <T> Response<T> getUserErrorResponse(String message)
    {
        return Response.<T>builder()
                .statusCode(2)
                .developerMessage(message)
                .build();
    }
}
